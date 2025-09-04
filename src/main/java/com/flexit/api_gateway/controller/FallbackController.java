package com.flexit.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/fallback/flexit-auth")
    public ResponseEntity<String> fallbackServiceUserAuth() {
        return ResponseEntity.ok("Service User authentication is currently unavailable. Please try again later.");
    }

    @GetMapping("/fallback/flexit-inventory-management")
    public ResponseEntity<String> fallbackServiceInventoryManagement() {
        return ResponseEntity.ok("Service Inventory Management is currently unavailable. Please try again later.");
    }
}