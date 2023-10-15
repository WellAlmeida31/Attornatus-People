package com.attornatus.people.domain.endereco.validation;

import com.attornatus.people.domain.endereco.dto.EnderecoDto;
import com.attornatus.people.domain.endereco.repository.EnderecoRepository;
import com.attornatus.people.domain.pessoa.Pessoa;
import com.attornatus.people.domain.pessoa.validation.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidacaoEnderecoExistente implements ValidacaoEndereco{

    private final EnderecoRepository enderecoRepository;
    @Override
    public void validacao(EnderecoDto enderecoDto, Pessoa pessoa) {
        if(enderecoRepository.findByCepAndNumeroIgnoreCaseAndPessoa(enderecoDto.getCep(),
                enderecoDto.getNumero(), pessoa).isPresent()){
            throw new ValidacaoException("Endereço já cadastrado no banco de dados para esta pessoa");
        }
    }
}
