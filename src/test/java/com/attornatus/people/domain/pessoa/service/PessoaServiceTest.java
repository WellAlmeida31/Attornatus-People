package com.attornatus.people.domain.pessoa.service;

import com.attornatus.people.domain.endereco.dto.ConsultaEndereco;
import com.attornatus.people.domain.endereco.dto.EnderecoDto;
import com.attornatus.people.domain.pessoa.dto.PessoaDto;
import com.attornatus.people.domain.pessoa.dto.Status;
import com.attornatus.people.domain.pessoa.service.PessoaService;
import com.attornatus.people.domain.pessoa.validation.ValidacaoException;
import com.attornatus.people.infrastructure.ConnectionRemote;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest()
class PessoaServiceTest {

    @Autowired
    private PessoaService pessoaService;
    @MockBean
    private ConnectionRemote connectionRemoteProxy;

    @Test
    @DisplayName("Efetuar cadastro de pessoa e adicionar um endereço")
    void cadastroPessoaEAdicionarEndereco() {
        ConsultaEndereco consultaEnd = getConsultaEndereco();
        PessoaDto pessoaDto = getPessoaDto("Teste AdicionarEndereco", null);
        EnderecoDto enderecoDto = getEnderecoDto("66821-000","11",false);

        when(connectionRemoteProxy.getCep("66821-000")).thenReturn(Response.ok(consultaEnd).build());

        var pessoa = pessoaService.cadastroPessoa(pessoaDto);
        var pessoaDtoResponse = pessoaService.adicionarEndereco(pessoa.getId(), List.of(enderecoDto));

        assertNotNull(pessoaDtoResponse);
        assertEquals(consultaEnd.getLocalidade(), pessoaDtoResponse.enderecos().get(0).getLocalidade());
        assertEquals(consultaEnd.getUf(), pessoaDtoResponse.enderecos().get(0).getUf());
    }

    @Test
    @DisplayName("Não deve efetuar o cadastro do mesmo endereço para uma pessoa")
    void cadastroPessoaEAdicionarEnderecoDuasVezez() {
        ConsultaEndereco consultaEnd = getConsultaEndereco();
        PessoaDto pessoaDto = getPessoaDto("Teste EnderecoDuasVezez", null);
        EnderecoDto enderecoDto = getEnderecoDto("66821-000","11",false);
        when(connectionRemoteProxy.getCep("66821-000")).thenReturn(Response.ok(consultaEnd).build());
        var pessoa = pessoaService.cadastroPessoa(pessoaDto);
        pessoaService.adicionarEndereco(pessoa.getId(), List.of(enderecoDto));
        assertThrows(ValidacaoException.class, () -> {
            pessoaService.adicionarEndereco(pessoa.getId(), List.of(enderecoDto));
        });
    }

    @Test
    @DisplayName("Não deve efetuar o cadastro da mesma pessoa")
    void cadastroDaMesmaPessoa() {
        ConsultaEndereco consultaEnd = getConsultaEndereco();
        PessoaDto pessoaDto = getPessoaDto("Teste MesmaPessoa", null);
        when(connectionRemoteProxy.getCep("66821-000")).thenReturn(Response.ok(consultaEnd).build());
        pessoaService.cadastroPessoa(pessoaDto);
        assertThrows(ValidacaoException.class, () -> {
            pessoaService.cadastroPessoa(pessoaDto);
        });
    }

    @Test
    @DisplayName("Não deve efetuar o cadastro de endereços ambíguos para uma pessoa")
    void cadastroDaPessoaComEnderecoAmbiguo() {
        ConsultaEndereco consultaEnd = getConsultaEndereco();
        var enderecoDto = getEnderecoDto("66821-000", "10", true);
        PessoaDto pessoaDto = getPessoaDto("Teste EnderecoAmbiguo", List.of(enderecoDto,enderecoDto.clone()));
        when(connectionRemoteProxy.getCep("66821-000")).thenReturn(Response.ok(consultaEnd).build());
        assertThrows(ValidacaoException.class, () -> {
            pessoaService.cadastroPessoa(pessoaDto);
        });
    }


    private static EnderecoDto getEnderecoDto(String cep, String num, Boolean principal) {
       return EnderecoDto.builder()
                .cep(cep)
                .numero(num)
                .principal(principal)
                .build();
    }

    private static PessoaDto getPessoaDto(String nome, List<EnderecoDto> enderecoDtos) {
        return new PessoaDto(1L, nome, "00000000000", Status.ACTIVE, LocalDate.now().minusYears(10), enderecoDtos != null ? enderecoDtos: List.of(EnderecoDto.builder()
                .id(1L)
                .numero("10")
                .cep("66821-000")
                .principal(true)
                .build()));
    }

    private static ConsultaEndereco getConsultaEndereco() {
        return ConsultaEndereco.builder()
                .cep("66821-000")
                .logradouro("Rodovia Augusto Montenegro")
                .complemento("do km 8,701 ao km 13,199 - lado ímpar")
                .bairro("Parque Guajará (Icoaraci)")
                .localidade("Belém")
                .siafi("0427")
                .gia("")
                .ibge("1501402")
                .uf("PA")
                .ddd("91")
                .build();
    }

}