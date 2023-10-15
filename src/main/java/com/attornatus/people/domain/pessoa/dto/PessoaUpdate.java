package com.attornatus.people.domain.pessoa.dto;

import com.attornatus.people.domain.pessoa.Pessoa;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PessoaUpdate(@NotEmpty @Size(min = 3, max = 255) String nome,
                           @NotNull LocalDate nascimento,
                           String telefone) {
    public PessoaUpdate(Pessoa pessoa) {this(pessoa.getNome(), pessoa.getNascimento(), pessoa.getTelefone());}
}
