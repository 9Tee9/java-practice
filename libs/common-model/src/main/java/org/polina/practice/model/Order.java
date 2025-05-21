package org.polina.practice.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
        private UUID id;
        private Status status;
        private Long userId;
        @NotEmpty(message = "Список товаров не может быть пустым")
        private List<String> items;

    }


