package com.retailbusiness.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public Mono<String> usersFallback() {
        return Mono.just("User service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/products")
    public Mono<String> productsFallback() {
        return Mono.just("Product service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/orders")
    public Mono<String> ordersFallback() {
        return Mono.just("Order service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/payments")
    public Mono<String> paymentsFallback() {
        return Mono.just("Payment service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/inventory")
    public Mono<String> inventoryFallback() {
        return Mono.just("Inventory service is temporarily unavailable. Please try again later.");
    }
}