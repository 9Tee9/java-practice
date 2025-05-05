package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders", schema = "shop2_schema")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @NotNull(message = "Покупатель должен быть указан")
    @JsonBackReference
    private Customer customer;
    @CreationTimestamp
    @Column(name = "order_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date orderDate;
    @Column(name = "shipping_address")
    @NotBlank(message = "Адрес должен быть указан")
    private String shippingAddress;
    @Column(name = "total_price")
    @DecimalMin(value = "0.01", message = "Общая сумма заказа должна быть больше 0")
    private BigDecimal totalPrice;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private Status orderStatus;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();
}
