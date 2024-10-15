package com.api.store.web.controller;

import com.api.store.domain.Feedback;
import com.api.store.domain.Founder;
import com.api.store.domain.FounderCountByDate;
import com.api.store.domain.dto.request.WaitlistRequest;
import com.api.store.domain.dto.response.JwtResponse;
import com.api.store.domain.dto.response.MessageResponse;
import com.api.store.domain.dto.response.WaitlistStatsResponse;
import com.api.store.domain.service.FeedbackService;
import com.api.store.domain.service.FounderService;
import com.api.store.web.security.services.UserDetailsImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {

    @Value("${store.sec.hcaptcha}")
    private String hCaptchaSecretKey;

    @Autowired
    ObjectMapper om;
    @Autowired
    FounderService founderService;

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/subscribe")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody WaitlistRequest waitlistRequest) throws IOException, InterruptedException {

        if (StringUtils.hasText(waitlistRequest.getCaptchaResponse())) {
            System.out.println("waitlistRequest.getCaptchaResponse(): " + waitlistRequest.getCaptchaResponse());
            System.out.println("waitlistRequest.getCaptchaResponse(): " + waitlistRequest.getEmail());
            System.out.println("waitlistRequest.getCaptchaResponse(): " + waitlistRequest.getFeedback());

            var sb = new StringBuilder();
            sb.append("response=");
            sb.append(waitlistRequest.getCaptchaResponse());
            sb.append("&secret=");
            sb.append(this.hCaptchaSecretKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://hcaptcha.com/siteverify"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(sb.toString())).build();

            HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
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

        if(founderService.existsByEmail(waitlistRequest.getEmail())) {
            Optional<Founder> founder = founderService.findByEmail(waitlistRequest.getEmail());
            if(founder.isPresent()){
                Founder _founder = founder.get();
                System.out.println("ID recuperado: "+ _founder.getId());
                String feedback = waitlistRequest.getFeedback();
                if (feedback != null && !feedback.trim().isEmpty()) {
                    feedbackService.save(new Feedback(_founder.getId(), feedback));
                }

            }
        }else {
            Founder founder = new Founder(waitlistRequest.getEmail());
            founder = founderService.save(founder);
            System.out.println("ID almacenado: "+ founder.getId());
            String feedback = waitlistRequest.getFeedback();
            if (feedback != null && !feedback.trim().isEmpty()) {
                feedbackService.save(new Feedback(founder.getId(), feedback));
            }

        }



        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        try{
            long founderCount = founderService.count();
            System.out.println("Total de usuarios = " + founderCount);

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Map<String, Long> map = new LinkedHashMap<>();
            for (int i = 1; i <= 15; i++) {
                LocalDate date = today.minusDays(15 - i);
                String dateString = date.format(formatter);
                map.put(dateString, -1L);
            }// hasta aca tenemos un map con las fechas de los últimos 15 días y valores en -1
            // map.forEach((key, value) -> System.out.println(key + ": " + value));



            // Obtener el inicio del día (medianoche pasada)
            LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(ZoneId.systemDefault()), LocalTime.MIDNIGHT);
            System.out.println(startOfDay + "- inicio del día (medianoche pasada)");
            // Calcular las fechas de inicio y fin para los últimos 15 días
            LocalDate endDate = LocalDate.now(); // Fecha actual
            LocalDate startDate = endDate.minusDays(14); // 15 días incluyendo hoy
            // Convertir a LocalDateTime para incluir toda la jornada del último día
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            List<FounderCountByDate> fcList = founderService.countFoundersGroupedByEntryDate(startDateTime, endDateTime);
            for (int i = fcList.size() - 1; i >= 0; i--) {
                FounderCountByDate fc = fcList.get(i);
                LocalDate newEntryDate = fc.getEntryDate().plusDays(1).toLocalDate();
                System.out.println("Fecha de entrada: " + newEntryDate.toString() + ", Total de fundadores: " + founderCount);
                map.put(newEntryDate.toString(), founderCount);
                founderCount = founderCount - fc.getTotalFounders();
            }// hasta aca tenemos un map con las fechas de los últimos 15 días y valores en -1 y solo si corresponde el acumulado del dia
            // map.forEach((key, value) -> System.out.println(key + ": " + value));


            // System.out.println("founderCount = " + founderCount);

            for (Map.Entry<String, Long> entry : map.entrySet()) {
                String key = entry.getKey();
                Long value = entry.getValue();
                if(value == -1L){
                    map.put(key, founderCount);
                }else{
                    founderCount = value;
                }
            }
            // aca tenemos el acumulado total de dias listos para graficar
            map.forEach((key, value) -> System.out.println(key + ": " + value));

//            long usersPerDay = founderService.countByEntryDateBetween(startOfDay, startOfDay.plusDays(1));
//            System.out.println("Users day 15 = " + usersPerDay);

            return ResponseEntity.ok(new WaitlistStatsResponse(map));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
