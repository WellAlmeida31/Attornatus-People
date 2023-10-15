package com.attornatus.people.domain.pessoa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PessoaDtoNew {

    private Long id;
    private String nome;
    private LocalDate nascimento;
    private String localidade;
    private String bairro;
    private String cep;
    private String uf;

    public PessoaDtoNew(Long id, String nome, LocalDate nascimento, String localidade, String bairro,
        String cep, String uf){
        this.id = id;
        this.nome = nome;
        this.nascimento = nascimento;
        this.localidade = localidade;
        this.bairro = bairro;
        this.cep = cep;
        this.uf = uf;
    }

}
