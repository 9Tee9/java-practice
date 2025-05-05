package org.polina.practice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.polina.practice.dto.UpsertProductRequest;
import org.polina.practice.entity.Customer;
import org.polina.practice.entity.Product;

import java.util.List;

public interface ProductService {
    void decreaseStock(Long id);
    List<Product> getAllProducts() throws JsonProcessingException;
    Product getProductById(Long id) throws JsonProcessingException;
    Product createProduct(Product product);
    Product updateProduct(Long id, UpsertProductRequest request);
    void deleteProductById(Long id);
}
