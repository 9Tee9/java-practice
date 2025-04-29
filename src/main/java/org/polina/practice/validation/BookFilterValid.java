package org.polina.practice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookFilterValid {
    String message() default "Хотя бы один фильтр должен быть указан";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
