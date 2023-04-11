package com.attornatus.people.infrastructure.exception;

import com.attornatus.people.domain.pessoa.validation.ValidacaoException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity tratarErroValidacao(ValidacaoException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(ex.getMessage(),null,ex.getCause()));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getFieldErrors()
                        .stream()
                        .map(ErrorMessage::new)
                        .toList());
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity tratarErroDeserializacaoJackson(UnrecognizedPropertyException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage("Não foi possivel executar a consulta ao endereço - "+ex.getMessage(),null,ex.getCause()));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity tratarErroDeReferencia(PropertyReferenceException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(ex.getMessage(), ex.getPropertyName(), ex.getCause(),null));
    }

}
