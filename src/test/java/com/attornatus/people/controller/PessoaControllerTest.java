package com.attornatus.people.controller;

import com.attornatus.people.domain.endereco.Endereco;
import com.attornatus.people.domain.endereco.EnderecoDto;
import com.attornatus.people.domain.pessoa.Pessoa;
import com.attornatus.people.domain.pessoa.PessoaDto;
import com.attornatus.people.domain.pessoa.PessoaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<PessoaDto> pessoaDtoJson;
    @Autowired
    private JacksonTester<Pessoa> pessoaJson;
    @MockBean
    private PessoaService pessoaServiceMock;
    private static final String URL_BASE = "/attornatus/pessoa";

    @Test
    @DisplayName("Requisição para cadastro de pessoa sem informar parâmetros, deve devolver status http 400")
    void cadastroPessoa1() throws Exception {
        assertThat(mockMvc.perform(post(URL_BASE+"/cadastro"))
                .andReturn()
                .getResponse()
                .getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Requisição para cadastro de pessoa com parâmetros corretos, deve devolver status http 201")
    void cadastroPessoa2() throws Exception {
        Pessoa pessoa = getPessoa();
        when(pessoaServiceMock.cadastroPessoa(any())).thenReturn(pessoa);
        JsonContent<PessoaDto> writePessoaDto = getPessoaDtoJsonContent("10", "66821-000",true);
        var apiResponse = mockMvc.perform(
                post(URL_BASE+"/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writePessoaDto
                                .getJson()))
                .andReturn()
                .getResponse();
        assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Requisição para cadastro de pessoa informando CEP inválido, deve devolver status http 400")
    void cadastroPessoa3() throws Exception {
        JsonContent<PessoaDto> writePessoaDto = getPessoaDtoJsonContent("10", "6682o-a00",true);
        assertThat(mockMvc.perform(post(URL_BASE+"/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writePessoaDto.getJson()))
                .andReturn()
                .getResponse()
                .getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Consulta dados de endereços de pessoa informando id corretamente, deve devolver status http 200")
    void getEnderecosPessoa1() throws Exception {
        var endereco = getPessoa().getEnderecos();
        when(pessoaServiceMock.getEnderecosPessoa(any(),any())).thenReturn(endereco);
        assertThat(mockMvc.perform(get(URL_BASE+"/"+1+"/enderecos")
                        .param("principal", Boolean.TRUE.toString()))
                .andReturn()
                .getResponse()
                .getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private static Pessoa getPessoa() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return Pessoa.builder()
                .id(1L)
                .nome("Test")
                .nascimento(LocalDate.parse("2023/04/11",formatter))
                .enderecos(List.of(Endereco.builder()
                        .id(1L)
                        .numero("10")
                        .cep("66821-000")
                        .logradouro("Rodovia Augusto Montenegro")
                        .complemento("do km 8,701 ao km 13,199 - lado impar")
                        .bairro("Parque Guajara (Icoaraci)")
                        .localidade("Belem")
                        .uf("PA")
                        .ddd("91")
                        .principal(true)
                        .build()))
                .build();
    }

    private JsonContent<PessoaDto> getPessoaDtoJsonContent(String numero, String cep, Boolean principal) throws IOException {
        return pessoaDtoJson.write(new PessoaDto("Test", LocalDate.now().minusYears(10), List.of(EnderecoDto.builder()
                .numero(numero)
                .cep(cep)
                .logradouro("Rodovia Augusto Montenegro")
                .complemento("do km 8,701 ao km 13,199 - lado impar")
                .bairro("Parque Guajara (Icoaraci)")
                .localidade("Belem")
                .uf("PA")
                .ddd("91")
                .principal(principal)
                .build())));
    }

}
