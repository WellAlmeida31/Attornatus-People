package com.attornatus.people.domain.pessoa.validation;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class ValidacaoException extends RuntimeException implements Serializable {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }

    public ValidacaoException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}
