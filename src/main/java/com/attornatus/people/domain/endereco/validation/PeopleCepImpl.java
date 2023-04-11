package com.attornatus.people.domain.endereco.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeopleCepImpl implements ConstraintValidator<PeopleCep, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("\\d{5}-\\d{3}");
    }
}
