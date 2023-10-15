package com.attornatus.people.domain.endereco.validation;


import com.attornatus.people.domain.endereco.dto.EnderecoDto;
import com.attornatus.people.domain.pessoa.Pessoa;

public interface ValidacaoEndereco {
    void validacao(EnderecoDto enderecoDto, Pessoa pessoa);
}
