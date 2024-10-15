package com.api.store.web.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StellarTomlController {

    @GetMapping("/.well-known/stellar.toml")
    public ResponseEntity<ClassPathResource> getStellarToml() {
        ClassPathResource resource = new ClassPathResource(".well-known/stellar.toml");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}