package com.detection.detectionservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/detection")
@CrossOrigin(origins = "*")
public class DetectionController {

    private final WebClient webClient;

    public DetectionController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5001").build();
    }

    @PostMapping("/detect")
    public ResponseEntity<String> detect(@RequestBody Map<String, String> request) {
        try {
            return webClient.post()
                    .uri("/detect")
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"Error connecting to backend: " + e.getMessage() + "\"}");
        }
    }
}
