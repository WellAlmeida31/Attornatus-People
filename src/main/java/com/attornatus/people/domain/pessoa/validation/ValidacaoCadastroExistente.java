package com.attornatus.people.domain.pessoa.validation;

import com.attornatus.people.domain.endereco.EnderecoDto;
import com.attornatus.people.domain.pessoa.PessoaDto;
import com.attornatus.people.domain.pessoa.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidacaoCadastroExistente implements ValidacaoDadosPessoa {

    private final PessoaRepository pessoaRepository;
    @Override
    public void validacao(PessoaDto pessoaDto) {
        if(pessoaRepository.existsByPessoaAndEnderecos(
                pessoaDto.nome(),
                pessoaDto.nascimento(),
                pessoaDto.enderecos()
                        .stream()
                        .map(EnderecoDto::getCep).toList(),
                pessoaDto.enderecos()
                        .stream()
                        .map(EnderecoDto::getNumero).toList()).size()>0){
            throw new ValidacaoException("Pessoa já cadastrada no banco de dados");
        }
    }
}
