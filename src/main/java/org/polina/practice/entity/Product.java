package org.polina.practice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Название товара не может быть пустым")
    private String name;
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @NotNull(message = "Цена должна быть указана")
    private BigDecimal price;
    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();
}
