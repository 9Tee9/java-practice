package org.polina.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.dto.UpsertProductRequest;
import org.polina.practice.entity.Product;
import org.polina.practice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    private ProductService productService;

    @Mock
    private Validator validator;

    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        productController = new ProductController(productService, objectMapper, validator);
    }
    @Test
    void whenGetAllProducts_thenReturnJsonProducts() throws Exception {
        Product product1 = createProduct(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0), 10);
        Product product2 = createProduct(2L, "Product 2", "Description 2", BigDecimal.valueOf(200.0), 5);

        List<Product> products = List.of(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<String> response = productController.getAllProducts();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$[0].productId")).longValue());
        assertEquals("Product 1", jsonContext.read("$[0].name"));
        assertEquals("Description 1", jsonContext.read("$[0].description"));
        assertEquals(100.0, jsonContext.read("$[0].price"));
        assertEquals(10, ((Number) jsonContext.read("$[0].quantityInStock")).intValue());

        assertEquals(2L, ((Number) jsonContext.read("$[1].productId")).longValue());
        assertEquals("Product 2", jsonContext.read("$[1].name"));
        assertEquals("Description 2", jsonContext.read("$[1].description"));
        assertEquals(200.0, jsonContext.read("$[1].price"));
        assertEquals(5, ((Number) jsonContext.read("$[1].quantityInStock")).intValue());

        verify(productService, times(1)).getAllProducts();
    }

    private Product createProduct(Long productId, String name, String description, BigDecimal price, Integer quantityInStock) {
        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantityInStock(quantityInStock);
        return product;
    }
    @Test
    void whenGetProductById_thenReturnJsonProduct() throws Exception {
        Product product = createProduct(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0), 10);
        when(productService.getProductById(1L)).thenReturn(product);

        ResponseEntity<String> response = productController.getProductById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.productId")).longValue());
        assertEquals("Product 1", jsonContext.read("$.name"));
        assertEquals("Description 1", jsonContext.read("$.description"));
        assertEquals(100.0, jsonContext.read("$.price"));
        assertEquals(10, ((Number) jsonContext.read("$.quantityInStock")).intValue());

        verify(productService, times(1)).getProductById(1L);
    }
    @Test
    void whenCreateProduct_thenReturnJsonCreatedProduct() throws Exception {
        Product product = createProduct(null, "Product 1", "Description 1", BigDecimal.valueOf(100.0), 10);
        Product createdProduct = createProduct(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0), 10);

        String productJson = objectMapper.writeValueAsString(product);
        when(productService.createProduct(any(Product.class))).thenReturn(createdProduct);

        ResponseEntity<String> response = productController.createProduct(productJson);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.productId")).longValue());
        assertEquals("Product 1", jsonContext.read("$.name"));
        assertEquals("Description 1", jsonContext.read("$.description"));
        assertEquals(100.0, jsonContext.read("$.price"));
        assertEquals(10, ((Number) jsonContext.read("$.quantityInStock")).intValue());

        verify(productService, times(1)).createProduct(any(Product.class));
    }
    @Test
    void whenUpdateProduct_thenReturnJsonUpdatedProduct() throws Exception {
        UpsertProductRequest request = new UpsertProductRequest();
        request.setPrice(BigDecimal.valueOf(200.0));
        request.setQuantityInStock(5);

        Product updatedProduct = createProduct(1L, "Product 1", "Description 1", BigDecimal.valueOf(200.0), 5);
        when(productService.updateProduct(eq(1L), any(UpsertProductRequest.class))).thenReturn(updatedProduct);

        ResponseEntity<String> response = productController.updateProduct(1L, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.productId")).longValue());
        assertEquals("Product 1", jsonContext.read("$.name"));
        assertEquals("Description 1", jsonContext.read("$.description"));
        assertEquals(200.0, jsonContext.read("$.price"));
        assertEquals(5, ((Number) jsonContext.read("$.quantityInStock")).intValue());

        verify(productService, times(1)).updateProduct(eq(1L), any(UpsertProductRequest.class));
    }
    @Test
    void whenDeleteProduct_thenReturnNoContent() {
        doNothing().when(productService).deleteProductById(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(productService, times(1)).deleteProductById(1L);
    }
}
