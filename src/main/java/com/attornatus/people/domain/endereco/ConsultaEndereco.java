package com.attornatus.people.domain.endereco;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConsultaEndereco {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private  String uf;
    private String ibge;
    private String gia;
    private  String ddd;
    private String siafi;
}
