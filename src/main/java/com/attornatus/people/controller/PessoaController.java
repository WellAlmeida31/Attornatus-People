package com.attornatus.people.controller;

import com.attornatus.people.domain.endereco.EnderecoDto;
import com.attornatus.people.domain.pessoa.PessoaDto;
import com.attornatus.people.domain.pessoa.PessoaService;
import com.attornatus.people.domain.pessoa.PessoaUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("attornatus/pessoa")
@Tag(name = "Cadastro de Pessoas", description = "Cadastro de Pessoas e Endereços")
public class PessoaController {

    private final PessoaService pessoaService;

    @Operation(
            summary = "Consulta lista de pessoas cadastradas",
            description = "<ul><li><p>Lista Pessoas e seus respectivos endereços cadastrados. A resposta é paginada</p></li></ul>" +
                    "<p><strong>page:</strong> Número da página de amostragem dos elementos.</p>" +
                    "<p><strong>size:</strong> Quantidade de itens amostrados por página.</p>" +
                    "<p><strong>sort:</strong> Nome do item pelo qual será feito o ordenamento (Opcional).</p>")
    @GetMapping("/list")
    public ResponseEntity<Page<PessoaDto>> listarPessoas(@PageableDefault(size = 10, sort = {"nome"}) Pageable pageable) {
        return ResponseEntity.ok(pessoaService.findAll(pageable).map(PessoaDto::new));
    }

    @Operation(
            summary = "Cadastro de pessoa e seus endereços",
            description = "<ul><li><p>Podem ser inseridos vários endereços mas somente um pode ser o principal<p></li>" +
                    "<li><p>Para Pessoa deve ser inserido Nome e Nascimento (<strong>yyyy-MM-dd</strong>)</p></li>" +
                    "<li><p>Para Endereço deve ser inserido o cep (no formato <strong>00000-000</strong>), o numero e principal (booleano) informando" +
                    " se o endereço é o principal</p></li>" +
                    "<p><strong>OBS:</strong> Será feita consulta e validação do cep para aquisição dos demais dados referentes ao endereço</p></ul>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PessoaDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/cadastro")
    public ResponseEntity cadastroPessoa(@RequestBody @Valid PessoaDto pessoaDto, UriComponentsBuilder uriBuilder) {
        var pessoa = pessoaService.cadastroPessoa(pessoaDto);
        return ResponseEntity
                .created(uriBuilder
                        .path("/attornatus/pessoa/{id}")
                        .buildAndExpand(pessoa.getId())
                        .toUri())
                .body(new PessoaDto(pessoa));
    }

    @Operation(
            summary = "Busca pelos dados de uma Pessoa através do ID",
            description = "<ul><li><p>Retorna os dados de Pessoa usando o ID, consulta do typo EAGER.</p></li>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PessoaDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{pessoaId}")
    public ResponseEntity<PessoaDto> getPessoa(@PathVariable Long pessoaId){
        return ResponseEntity.ok(new PessoaDto(pessoaService.getPessoa(pessoaId)));
    }

    @Operation(
            summary = "Busca pelos dados de endereço de uma pessoa pelo ID",
            description = "<ul><li><p>Retorna os dados de endereço de uma pessoa passando o ID </p></li>" +
                    "<li><p>Informe o paremetro booleano 'principal' para carregar somente o endereço principal ou todos os endereços cadastrados.</p></li></ul>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = EnderecoDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{pessoaId}/enderecos")
    public ResponseEntity<List<EnderecoDto>> getEnderecosPessoa(@PathVariable Long pessoaId, @RequestParam(required = true) Boolean principal){
        return ResponseEntity.ok(pessoaService.getEnderecosPessoa(pessoaId, principal).stream().map(EnderecoDto::new).toList());
    }

    @Operation(
            summary = "Adiciona endereços a uma pessoa",
            description = "<ul><li><p>Adiciona um endereço para uma pessoa através de seu ID.</p></li>" +
                    "<li><p>Lembrando que uma pessoa só deve ter um endereço principal, portanto, ao adicionar um novo endereco principal (true) " +
                    "os endereços anteriormente cadastrados a essa pessoa serão secundários (principal = false).</p></li>" +
                    "<p><strong>OBS:</strong> Será feita consulta e validação do cep para aquisição dos demais dados referentes ao endereço</p></ul>")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = PessoaDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("{pessoaId}/enderecos/adicionar")
    public ResponseEntity<PessoaDto> adicionarEndereco(@PathVariable Long pessoaId, @RequestBody @Valid List<EnderecoDto> enderecos){
        return ResponseEntity.ok(pessoaService.adicionarEndereco(pessoaId, enderecos));
    }

    @Operation(
            summary = "Atualizar dados de uma pessoa",
            description = "<ul><li><p>Atualização dos dados referentes a uma pessoa atravéz de seu ID.</p></li></ul>")
    @PutMapping("{pessoaId}/editar")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = PessoaUpdate.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<PessoaUpdate> updatePessoa(@PathVariable Long pessoaId, @RequestBody @Valid PessoaUpdate pessoaUpdate){
        return ResponseEntity.ok(pessoaService.updatePessoa(pessoaId,pessoaUpdate));
    }

    @Operation(
            summary = "Atualiza os dados de um endereco de uma pessoa",
            description = "<ul><li><p>Atualização de um endereço de uma pessoa pelos seus respectivos IDs.</p></li>" +
                    "<p><strong>OBS:</strong> Será feita consulta e validação do cep para aquisição dos demais dados referentes ao endereço</p></ul>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = EnderecoDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("{pessoaId}/enderecos/{enderecoId}/editar")
    public ResponseEntity<EnderecoDto> updateEnderecoPessoa(@PathVariable Long pessoaId, @PathVariable Long enderecoId, @RequestBody @Valid EnderecoDto enderecoDto){
        return ResponseEntity.ok(pessoaService.updateEnderecoPessoa(pessoaId, enderecoId, enderecoDto));
    }
}
