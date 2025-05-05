package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customers", schema = "shop2_schema")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "first_name")
    @NotBlank(message = "Имя должно быть указано")
    private String firstName;
    @Column(name = "last_name")
    @NotBlank(message = "Фамилия должна быть указана")
    private String lastName;
    @Email(message = "Невалидный формат Email")
    @NotBlank(message = "Email должен быть указан")
    private String email;
    @Column(name = "contact_number")
    @Pattern(regexp = "^(\\+7|8)[0-9]{10}$",
            message = "Некорректный формат контактного номера")
    @NotBlank(message = "Номер телефона должен быть указан")
    private String contactNumber;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();
}
