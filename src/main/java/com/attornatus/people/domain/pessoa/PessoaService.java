package com.attornatus.people.domain.pessoa;

import com.attornatus.people.domain.endereco.*;
import com.attornatus.people.domain.endereco.validation.ValidacaoEndereco;
import com.attornatus.people.domain.pessoa.validation.ValidacaoDadosPessoa;
import com.attornatus.people.infrastructure.ConnectionRemote;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    private final EnderecoRepository enderecoRepository;

    private final ConnectionRemote connectionRemoteProxy;
    private final ModelMapper modelMapper = new ModelMapper();

    private final List<ValidacaoDadosPessoa> validacaoDadosPessoa;

    private final List<ValidacaoEndereco> validacaoEndereco;


    public Page<Pessoa> findAll(Pageable pageable){
        return pessoaRepository.findAll(pageable);
    }
    @Transactional()
    public Pessoa cadastroPessoa(PessoaDto pessoaDto){
        validacaoDadosPessoa.forEach(val -> val.validacao(pessoaDto));
        consultaEnderecos(pessoaDto.enderecos());
        return pessoaRepository.save(new Pessoa(pessoaDto));
    }

    public Pessoa getPessoa(Long id){
        return pessoaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Endereco> getEnderecosPessoa(Long id, Boolean principal){
        if(principal) return singletonList(pessoaRepository.findEnderecoPrincipalByPessoaId(id).orElseThrow(EntityNotFoundException::new));
        return pessoaRepository.findAllEnderecosByPessoaId(id);
    }

    @Transactional
    public PessoaDto adicionarEndereco(Long pessoaId, List<EnderecoDto> enderecos){
        var pessoa = pessoaRepository.findById(pessoaId).orElseThrow(EntityNotFoundException::new);
        validacaoEndereco.forEach(val -> enderecos.forEach(e-> val.validacao(e,pessoa)));

        consultaEnderecos(enderecos);

        if(enderecos.stream().anyMatch(EnderecoDto::getPrincipal)){
            enderecoRepository.updatePrincipalToFalseByPessoa(pessoaId);
        }

        pessoa.setEnderecos(enderecos.stream().map(enderecoDto -> new Endereco(enderecoDto,pessoa)).toList());
        return new PessoaDto(pessoa);
    }

    @Transactional
    public PessoaUpdate updatePessoa(Long pessoaId, PessoaUpdate pessoaUpdate){
        var pessoa = pessoaRepository.getReferenceById(pessoaId);
        pessoa.updatePessoa(pessoaUpdate);
        return new PessoaUpdate(pessoa);
    }

    @Transactional
    public EnderecoDto updateEnderecoPessoa(Long pessoaId, Long enderecoId, EnderecoDto enderecoDto){
        Endereco endereco = enderecoRepository.getReferenceById(enderecoId);
        consultaEnderecos(List.of(enderecoDto));
        if(enderecoDto.getPrincipal()){
            enderecoRepository.updatePrincipalToFalseByPessoa(pessoaId);
        }

        endereco.updateEndereco(enderecoDto);

        return enderecoDto;
    }

    private void consultaEnderecos(List<EnderecoDto> enderecos) {
        enderecos.forEach(e -> {
            var endereco = (ConsultaEndereco) getClientData(connectionRemoteProxy.getCep(e.getCep()), ConsultaEndereco.class);
            this.modelMapper.map(endereco, EnderecoDto.class).forEach(e::set);
        });
    }
    private <R> Object getClientData(Response client, Class<R> clazz) {
        R cd = client.readEntity(clazz);
        //client.close();
        return (R) cd;
    }
}
