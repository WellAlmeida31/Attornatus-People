package com.attornatus.people.domain.endereco.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {PeopleCepImpl.class} )
public @interface PeopleCep {

    String message() default "O CEP digitado não é válido - Use o formato: 00000-000";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
