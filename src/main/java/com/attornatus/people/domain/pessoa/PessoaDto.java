package com.attornatus.people.domain.pessoa;

import com.attornatus.people.domain.endereco.EnderecoDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record PessoaDto(@NotEmpty @Size(min = 3, max = 255) String nome, @NotNull LocalDate nascimento, @Valid @NotNull List<EnderecoDto> enderecos) {
    public PessoaDto(Pessoa pessoa){
        this(pessoa.getNome(), pessoa.getNascimento(), pessoa.getEnderecos().stream().map(EnderecoDto::new).toList());
    }

}
