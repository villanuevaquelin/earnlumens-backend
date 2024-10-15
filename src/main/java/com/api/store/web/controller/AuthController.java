package com.api.store.web.controller;

import com.api.store.domain.*;
import com.api.store.domain.dto.request.LoginRequest;
import com.api.store.domain.dto.request.SignupRequest;
import com.api.store.domain.dto.request.VerifyRequest;
import com.api.store.domain.dto.response.JwtResponse;
import com.api.store.domain.dto.response.MessageResponse;
import com.api.store.domain.service.StellarService;
import com.api.store.domain.service.TempUserService;
import com.api.store.domain.service.WalletService;
import com.api.store.web.security.EncryptorDecoder;
import com.api.store.web.security.jwt.JwtUtils;
import com.api.store.web.security.services.UserDetailsImpl;
import com.api.store.web.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    WalletService walletService;

    @Autowired
    StellarService stellarService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    TempUserService tempUserService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EncryptorDecoder encryptorDecoder;

    @Autowired
    ObjectMapper om;

    @Value("${store.sec.hcaptcha}")
    private String hCaptchaSecretKey;

    @Value("${store.sec.cookieDomain}")
    private String cookieDomain;

    @Value("${store.sec.cookieSecure}")
    private boolean cookieSecure;

    @Value("${store.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtMasked = jwtUtils.generateJwtTokenMasked(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            String jwt = jwtUtils.getJWTTokenUnmasked(jwtMasked);
            String jwtEncripted = encryptorDecoder.encryptCookie(jwt, userDetails.getXlmAddress());

            System.out.println("creating a cookie");
            // create a cookie
            Cookie cookie = new Cookie("HOT",jwtEncripted);

            // expires in 13 days
            cookie.setMaxAge(jwtExpirationMs/1000);

            // optional properties
            cookie.setSecure(cookieSecure);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setDomain(cookieDomain);

            // add cookie to response
            response.addCookie(cookie);

        /*return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                encryptor.decryptUsername(userDetails.getUsername()),
                encryptor.decrypt(userDetails.getXlmAddress()),
                roles));*/



            return ResponseEntity.ok(new JwtResponse(jwtMasked, userDetails.getUsername(),
                    userDetails.getUsername(),
                    userDetails.getXlmAddress(),
                    roles));
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException, InterruptedException {

        if (StringUtils.hasText(signUpRequest.getCaptchaResponse())) {
            var sb = new StringBuilder();
            sb.append("response=");
            sb.append(signUpRequest.getCaptchaResponse());
            sb.append("&secret=");
            sb.append(this.hCaptchaSecretKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://hcaptcha.com/siteverify"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofSeconds(10))
                    .POST(BodyPublishers.ofString(sb.toString())).build();

            HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    BodyHandlers.ofString());
            System.out.println("http response status: " + response.statusCode());
            System.out.println("body: " + response.body());

            JsonNode hCaptchaResponseObject = this.om.readTree(response.body());
            boolean success = hCaptchaResponseObject.get("success").asBoolean();

            System.out.println("Respuesta Booleana: " + success);

            // timestamp of the captcha (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
            JsonNode jsonNode = hCaptchaResponseObject.get("challenge_ts");
            if (jsonNode != null) {
                String challengeTs = jsonNode.asText();
                System.out.println("challenge_ts=" + challengeTs);
            }

            // the hostname of the site where the captcha was solved
            jsonNode = hCaptchaResponseObject.get("hostname");
            if (jsonNode != null) {
                String hostname = jsonNode.asText();
                System.out.println("hostname=" + hostname);
            }

            // optional: whether the response will be credited
            jsonNode = hCaptchaResponseObject.get("credit");
            if (jsonNode != null) {
                boolean credit = jsonNode.asBoolean();
                System.out.println("credit=" + credit);
            }

            JsonNode errorCodesArray = hCaptchaResponseObject.get("error-codes");
            if (errorCodesArray != null) {
                System.out.println("error-codes");
                for (JsonNode errorCode : errorCodesArray) {
                    System.out.println("  " + errorCode.asText());
                }
            } else {
                System.out.println("no errors");
            }

            if (!success) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Captcha error, try again!"));
            }
        }

        // Se verifica que el usuario no este registrado
        if (userDetailsService.existsByUsername(encryptorDecoder.encryptUsername(signUpRequest.getUsername()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already registered!"));
        }

        // se genera codigo de 6 numeros
        // se envia codigo por email
        String codex = "423823";

        // se chequea en la base temporal si el usuario existe y se lo guarda o actualiza.
        Optional<TempUser> tempData = tempUserService.findByUsername(signUpRequest.getUsername());
        if (tempData.isPresent()) {
            TempUser _tUser = tempData.get();
            _tUser.setPassword(signUpRequest.getPassword());
            _tUser.setCode(codex);
            _tUser.setLastModifiedDate(new Date());
            tempUserService.save(_tUser);
        } else {
            TempUser tempUser = new TempUser(codex, signUpRequest.getUsername(), signUpRequest.getPassword());
            tempUserService.save(tempUser);
        }


        // hasta aca termina el registro temporal. y empieza la verificacion


        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/verify")
    @Transactional
    public ResponseEntity<?> verifyAccount(@Valid @RequestBody VerifyRequest verifyRequest) throws IOException, InterruptedException {

        try {
            if (StringUtils.hasText(verifyRequest.getUsername()) && StringUtils.hasText(verifyRequest.getCodex())) {
                Optional<TempUser> tempData = tempUserService.findByUsername(verifyRequest.getUsername());
                if (tempData.isPresent()) {
                    TempUser _tUser = tempData.get();
                    if (_tUser.getCodetry().equals("first try")) {
                        _tUser.setCodetry("taken");
                        tempUserService.save(_tUser);
                    } else {
                        if (StringUtils.hasText(verifyRequest.getCaptchaResponse())) {
                            var sb = new StringBuilder();
                            sb.append("response=");
                            sb.append(verifyRequest.getCaptchaResponse());
                            sb.append("&secret=");
                            sb.append(this.hCaptchaSecretKey);

                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create("https://hcaptcha.com/siteverify"))
                                    .header("Content-Type", "application/x-www-form-urlencoded")
                                    .timeout(Duration.ofSeconds(10))
                                    .POST(BodyPublishers.ofString(sb.toString())).build();

                            HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5))
                                    .build();

                            HttpResponse<String> response = httpClient.send(request,
                                    BodyHandlers.ofString());
                            System.out.println("http response status: " + response.statusCode());
                            System.out.println("body: " + response.body());

                            JsonNode hCaptchaResponseObject = this.om.readTree(response.body());
                            boolean success = hCaptchaResponseObject.get("success").asBoolean();

                            System.out.println("Respuesta Booleana: " + success);

                            // timestamp of the captcha (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
                            JsonNode jsonNode = hCaptchaResponseObject.get("challenge_ts");
                            if (jsonNode != null) {
                                String challengeTs = jsonNode.asText();
                                System.out.println("challenge_ts=" + challengeTs);
                            }

                            // the hostname of the site where the captcha was solved
                            jsonNode = hCaptchaResponseObject.get("hostname");
                            if (jsonNode != null) {
                                String hostname = jsonNode.asText();
                                System.out.println("hostname=" + hostname);
                            }

                            // optional: whether the response will be credited
                            jsonNode = hCaptchaResponseObject.get("credit");
                            if (jsonNode != null) {
                                boolean credit = jsonNode.asBoolean();
                                System.out.println("credit=" + credit);
                            }

                            JsonNode errorCodesArray = hCaptchaResponseObject.get("error-codes");
                            if (errorCodesArray != null) {
                                System.out.println("error-codes");
                                for (JsonNode errorCode : errorCodesArray) {
                                    System.out.println("  " + errorCode.asText());
                                }
                            } else {
                                System.out.println("no errors");
                            }

                            if (!success) {
                                return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponse("Error: Captcha fail, try again!"));
                            }
                        } else {
                            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
                            //solicitar hcaptcha
                        }
                    }

                    if (_tUser.getCode().equals(verifyRequest.getCodex())) {
                        // copiar tempuser en user crear wallet, eliminar tempuser

                        // Create new user's account
                        User user = new User(encryptorDecoder.encryptUsername(_tUser.getUsername()),
                                encoder.encode(_tUser.getPassword()));

                        // Create new user's wallet
                        Wallet wallet = stellarService.createAccount(_tUser.getUsername());
                        walletService.save(wallet);

                        String endKey = wallet.getSecretKey().substring(wallet.getSecretKey().length() - 7);
                        String hiddenEndKey = encryptorDecoder.hideEndKey(endKey);
                        user.setTimeZone(encryptorDecoder.encrypt(hiddenEndKey));

                        user.setWpk(encryptorDecoder.encrypt(wallet.getPublicKey()));

                        Set<Role> roles_ = new HashSet<>();

                        Role userRole = userDetailsService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles_.add(userRole);

                        user.setRoles(roles_);
                        userDetailsService.save(user);

                        tempUserService.deleteById(_tUser.getId());

                        //return new ResponseEntity<>(HttpStatus.OK);
                        try {
                            Authentication authentication = authenticationManager.authenticate(
                                    new UsernamePasswordAuthenticationToken(_tUser.getUsername(), _tUser.getPassword()));

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            String jwt = jwtUtils.generateJwtTokenMasked(authentication);

                            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                            List<String> roles = userDetails.getAuthorities().stream()
                                    .map(item -> item.getAuthority())
                                    .collect(Collectors.toList());

                            /*return ResponseEntity.ok(new JwtResponse(jwt,
                                    userDetails.getId(),
                                    encryptor.decryptUsername(userDetails.getUsername()),
                                    encryptor.decrypt(userDetails.getXlmAddress()),
                                    roles));*/

                            return ResponseEntity.ok(new JwtResponse(jwt,
                                    encryptorDecoder.decrypt(userDetails.getTimeZone()),
                                    userDetails.getSecretKey(),
                                    userDetails.getXlmAddress(),
                                    roles));
                        } catch (BadCredentialsException ex) {
                            return new ResponseEntity<>(HttpStatus.CONFLICT);
                        }
                    } else {
                        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                        //code fail
                    }
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    // Account not present
                }
            } else {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
                //not valid request
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> readAllCookies(
            @CookieValue(name = "HOT", defaultValue = "default-user-id") String httpOnlyCookieEncryptedValue) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(jwtUtils.hasValidHttpOnlyCookie(userDetails, httpOnlyCookieEncryptedValue)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
