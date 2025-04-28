package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.polina.practice.views.Views;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.UserSummary.class, Views.OrderSummary.class})
    private Long id;
    @NotBlank(message = "Имя должно быть указано")
    @JsonView({Views.UserSummary.class, Views.OrderDetails.class})
    private String name;
    @JsonView({Views.UserSummary.class, Views.OrderDetails.class})
    @NotBlank(message = "Email должен быть указан")
    @Email(message = "Невалидный формат email")
    private String email;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonView(Views.UserDetails.class)
    private List<Order> orders = new ArrayList<>();
}
