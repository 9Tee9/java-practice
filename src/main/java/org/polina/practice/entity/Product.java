package org.polina.practice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "products", schema = "shop2_schema")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @NotBlank(message = "Название продукта не может быть пустым")
    private String name;
    @NotBlank(message = "Описание продукта не может быть пустым")
    private String description;
    @DecimalMin(value = "0.01", message = "Цена товара должна быть больше 0")
    @NotNull(message = "Цена должна быть указана")
    private BigDecimal price;
    @Column(name = "quantity_in_stock")
    @NotNull(message = "Количество товара на складе должно быть указано")
    @PositiveOrZero(message = "Количество товара на складе должно быть положительным числом или равняться 0")
    private Integer quantityInStock;
}
