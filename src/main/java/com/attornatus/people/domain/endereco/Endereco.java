package com.attornatus.people.domain.endereco;

import com.attornatus.people.domain.endereco.dto.EnderecoDto;
import com.attornatus.people.domain.pessoa.Pessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;

@Entity(name = "Endereco")
@Table(name = "endereco")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String logradouro;
    @NotBlank
    private String cep;
    private String complemento;
    private String localidade;
    private String bairro;
    private String uf;
    private String ddd;
    @NotBlank
    @Size(min = 1, max = 10)
    private String numero;
    @NotNull
    private Boolean principal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pessoa_id", nullable = false)
    private Pessoa pessoa;

    public Endereco(EnderecoDto enderecoDto, Pessoa pessoa) {
        this.logradouro = enderecoDto.getLogradouro();
        this.bairro = enderecoDto.getBairro();
        this.cep = enderecoDto.getCep();
        this.complemento = enderecoDto.getComplemento();
        this.localidade = enderecoDto.getLocalidade();
        this.numero = enderecoDto.getNumero();
        this.principal = enderecoDto.getPrincipal();
        this.uf = enderecoDto.getUf();
        this.ddd = enderecoDto.getDdd();
        this.pessoa = pessoa;
    }

    public void updateEndereco(EnderecoDto enderecoDto){
        if(!isNullOrEmpty(enderecoDto.getLogradouro())) this.logradouro = enderecoDto.getLogradouro();
        if(!isNullOrEmpty(enderecoDto.getBairro()))  this.bairro = enderecoDto.getBairro();
        if(!isNullOrEmpty(enderecoDto.getCep()))  this.cep = enderecoDto.getCep();
        if(!isNullOrEmpty(enderecoDto.getComplemento())) this.complemento = enderecoDto.getComplemento();
        if(!isNullOrEmpty(enderecoDto.getLocalidade())) this.localidade = enderecoDto.getLocalidade();
        if(!isNullOrEmpty(enderecoDto.getUf())) this.numero = enderecoDto.getUf();
        if(!isNullOrEmpty(enderecoDto.getDdd())) this.ddd = enderecoDto.getDdd();
        if(!isNullOrEmpty(enderecoDto.getNumero())) this.numero = enderecoDto.getNumero();
        if(!isNull(enderecoDto.getPrincipal())) this.principal = enderecoDto.getPrincipal();
    }

}
