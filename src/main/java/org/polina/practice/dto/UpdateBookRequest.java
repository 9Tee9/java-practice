package org.polina.practice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBookRequest {
    @Size(min = 1, max = 100, message = "Название не может быть меньше {min} и больше {max} символов!")
    private String title;
    @NotBlank(message = "Описание книги не может быть пустым!")
    @Size(min = 1, max = 500, message = "Описание не может быть меньше {min} и больше {max} символов!")
    private String description;
}
