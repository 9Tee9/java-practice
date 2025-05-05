package org.polina.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.polina.practice.entity.Status;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpsertOrderRequest {
    @NotNull(message = "Статус заказа должен быть указан")
    private Status orderStatus;
}
