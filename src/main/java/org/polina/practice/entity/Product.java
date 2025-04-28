package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.polina.practice.views.Views;

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
    @JsonView({Views.UserDetails.class,
            Views.OrderSummary.class,
            Views.ProductSummary.class})
    private Long id;
    @NotBlank(message = "Название товара не может быть пустым")
    @JsonView({Views.UserDetails.class,
            Views.OrderSummary.class,
            Views.ProductSummary.class})
    private String name;
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @NotNull(message = "Цена должна быть указана")
    @JsonView({Views.UserDetails.class,
            Views.OrderSummary.class,
            Views.ProductSummary.class})
    private BigDecimal price;
    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Order> orders = new ArrayList<>();
}
