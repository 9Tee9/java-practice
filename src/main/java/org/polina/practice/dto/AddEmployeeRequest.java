package org.polina.practice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddEmployeeRequest {
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 3, max = 70, message =
            "Имя сотрудника не может быть меньше {min} и больше {max} символов!")
    private String firstName;
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 3, max = 70, message =
            "Фамилия сотрудника не может быть меньше {min} и больше {max} символов!")
    private String lastName;
    @NotBlank(message = "Должность должна быть указана")
    @Size(min = 3, max = 50, message =
            "Название позиции не может быть меньше {min} и больше {max} символов!")
    private String position;
    @DecimalMin(value = "0.0", inclusive = false, message = "Зарплата должна быть больше нуля")
    @NotNull(message = "Зарплата должна быть указана")
    private BigDecimal salary;
    @NotNull(message = "Id отдела должно быть указано")
    @Positive(message = "Id должно быть положительным числом")
    private Long departmentId;
}
