package com.attornatus.people.domain.pessoa.dto;

import com.attornatus.people.domain.endereco.dto.EnderecoDto;
import com.attornatus.people.domain.pessoa.Pessoa;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record PessoaDto(@JsonProperty(access = JsonProperty.Access.READ_ONLY) Long id,
                        @NotEmpty @Size(min = 3, max = 255) String nome,
                        String telefone,
                        @JsonProperty(access = JsonProperty.Access.READ_ONLY) Status status,
                        @NotNull LocalDate nascimento,
                        @Valid @NotNull List<EnderecoDto> enderecos) {
    public PessoaDto(Pessoa pessoa){
        this(pessoa.getId(), pessoa.getNome(), pessoa.getTelefone(), pessoa.getStatus(), pessoa.getNascimento(), pessoa.getEnderecos().stream().map(EnderecoDto::new).toList());
    }

}
