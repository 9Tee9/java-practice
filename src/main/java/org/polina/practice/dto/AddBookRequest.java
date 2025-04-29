package org.polina.practice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddBookRequest {
    @NotBlank(message = "Название книги должно быть указано!")
    @Size(min = 1, max = 100, message = "Название не может быть меньше {min} и больше {max} символов!")
    private String title;
    @NotBlank(message = "Описание книги не может быть пустым!")
    @Size(min = 1, max = 3000, message = "Описание не может быть меньше {min} и больше {max} символов!")
    private String description;
    @NotEmpty(message = "У книги должен быть указан хотя бы 1 автор!")
    private List<Long> authorIds;
}
