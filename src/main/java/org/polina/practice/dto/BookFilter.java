package org.polina.practice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.polina.practice.validation.BookFilterValid;

@Data
@NoArgsConstructor
@BookFilterValid
public class BookFilter {
    @Positive(message = "Размер страницы должен быть положительным!")
    private Integer pageSize;
    @Min(value = 0, message = "Номер страницы должен быть неотрицательным!")
    private Integer pageNumber;
}

