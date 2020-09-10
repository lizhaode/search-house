package com.lizhao.searchhouse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Ping {

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        log.info("receive ping");
        return ResponseEntity.ok("success");
    }
}
