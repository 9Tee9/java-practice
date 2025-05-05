package org.polina.practice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpsertProductRequest {
    @DecimalMin(value = "0.01", message = "Цена товара должна быть больше 0")
    private BigDecimal price;
    @PositiveOrZero(message = "Количество товара на складе должно быть положительным числом или равняться 0")
    private Integer quantityInStock;
}
