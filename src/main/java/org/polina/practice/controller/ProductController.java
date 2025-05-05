package org.polina.practice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.UpsertProductRequest;
import org.polina.practice.entity.Product;
import org.polina.practice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @GetMapping("/all")
    public ResponseEntity<String> getAllProducts() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(productService.getAllProducts()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(productService.getProductById(id)));
    }
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String productJson) throws JsonProcessingException {
        Product product = objectMapper.readValue(productJson, Product.class);
        validator.validate(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.writeValueAsString(productService.createProduct(product)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody @Valid UpsertProductRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(productService.updateProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}

