package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.polina.practice.views.Views;

import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;
    @JsonView(Views.Summary.class)
    @NotBlank(message = "Имя должно быть указано")
    private String name;
    @JsonView(Views.Summary.class)
    @NotBlank(message = "Email должен быть указан")
    @Email(message = "Невалидный формат email")
    private String email;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.Details.class)
    private List<Order> orders;
}
