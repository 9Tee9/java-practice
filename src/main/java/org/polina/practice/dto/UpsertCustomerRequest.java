package org.polina.practice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpsertCustomerRequest {
    @Email(message = "Невалидный формат Email")
    private String email;
    @Pattern(regexp = "^(\\+7|8)[0-9]{10}$",
            message = "Некорректный формат контактного номера")
    private String contactNumber;
}
