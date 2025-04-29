package org.polina.practice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.polina.practice.dto.BookFilter;

public class BookFilterValidValidator implements ConstraintValidator<BookFilterValid, BookFilter> {

    @Override
    public boolean isValid(BookFilter bookFilter, ConstraintValidatorContext constraintValidatorContext) {
        return bookFilter.getPageSize() != null
                || bookFilter.getPageNumber() != null;
    }
}
