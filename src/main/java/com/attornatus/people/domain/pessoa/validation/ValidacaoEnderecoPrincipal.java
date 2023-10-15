package com.attornatus.people.domain.pessoa.validation;

import com.attornatus.people.domain.endereco.dto.EnderecoDto;
import com.attornatus.people.domain.pessoa.dto.PessoaDto;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoEnderecoPrincipal implements ValidacaoDadosPessoa {
    @Override
    public void validacao(PessoaDto pessoaDto) {
        if(pessoaDto.enderecos().stream().map(EnderecoDto::getPrincipal).filter(p -> p).count()>1){
            throw new ValidacaoException("Item: principal, Value: true - Mais de um endereço como principal");
        }
    }
}
