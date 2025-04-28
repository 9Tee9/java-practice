package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.polina.practice.views.Views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private Long id;
    @Column(name = "total_price")
    @DecimalMin(value = "0.01", message = "Общая сумма заказа должна быть больше 0")
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private Status status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonView({Views.OrderDetails.class})
    private User user;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @NotNull(message = "Список товаров не может быть пустым!")
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private List<Product> products = new ArrayList<>();
}
