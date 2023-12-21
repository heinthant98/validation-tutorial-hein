package com.validationtask.task2.controller;

import com.validationtask.task2.request.ProductRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProductController {

    @PostMapping("/products")
    public Map<String, String> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return Map.of("message", "successfully created");
    }
}
