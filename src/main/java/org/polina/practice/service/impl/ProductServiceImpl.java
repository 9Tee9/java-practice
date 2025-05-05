package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.UpsertProductRequest;
import org.polina.practice.entity.Product;
import org.polina.practice.exception.ProductNotFoundException;
import org.polina.practice.exception.ProductNotInStockException;
import org.polina.practice.repository.ProductRepository;
import org.polina.practice.service.ProductService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void decreaseStock(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new ProductNotFoundException(MessageFormat
                        .format("Товар с id {0} не найден!", id)));
        if (product.getQuantityInStock() <= 0) {
            throw new ProductNotInStockException("Товар " + product.getName() + " отсутствует на складе");
        }
        product.setQuantityInStock(product.getQuantityInStock() - 1);
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->
                new ProductNotFoundException(MessageFormat
                        .format("Товар с id {0} не найден!", id)));
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, UpsertProductRequest request) {
        Product updatedProduct = productRepository.findById(id).orElseThrow(()->
                new ProductNotFoundException(MessageFormat
                        .format("Товар с id {0} не найден!", id)));
        if (request.getPrice() != null) {
            updatedProduct.setPrice(request.getPrice());
        }
        if (request.getQuantityInStock() != null) {
            updatedProduct.setQuantityInStock(request.getQuantityInStock());
        }
        return productRepository.save(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new ProductNotFoundException(MessageFormat
                        .format("Товар с id {0} не найден!", id)));
        productRepository.deleteById(product.getProductId());
    }
}
