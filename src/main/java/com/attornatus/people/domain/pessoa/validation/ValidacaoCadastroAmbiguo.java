package com.attornatus.people.domain.pessoa.validation;

import com.attornatus.people.domain.endereco.dto.EnderecoDto;
import com.attornatus.people.domain.pessoa.dto.PessoaDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class ValidacaoCadastroAmbiguo implements ValidacaoDadosPessoa {
    @Override
    public void validacao(PessoaDto pessoaDto) {
        long count = new HashSet<EnderecoDto>(pessoaDto.enderecos()).size();
        if(count < pessoaDto.enderecos().size()) throw new ValidacaoException("Informações de endereço ambíguas");
    }
}
