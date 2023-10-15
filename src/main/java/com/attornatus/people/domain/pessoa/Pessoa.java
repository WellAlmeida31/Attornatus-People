package com.attornatus.people.domain.pessoa;

import com.attornatus.people.domain.endereco.Endereco;
import com.attornatus.people.domain.pessoa.dto.PessoaDto;
import com.attornatus.people.domain.pessoa.dto.PessoaUpdate;
import com.attornatus.people.domain.pessoa.dto.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;

@Entity(name = "Pessoa")
@Table(name = "pessoa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @NotBlank
    private String nome;
    private String telefone;
    @Enumerated
    private Status status;
    private LocalDate nascimento;
    @OneToMany(mappedBy="pessoa", cascade = CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();

    public Pessoa(PessoaDto pessoaDto){
        this.nome = pessoaDto.nome();
        this.nascimento = pessoaDto.nascimento();
        this.telefone = pessoaDto.telefone();
        this.status = Status.INACTIVE;
        this.enderecos.addAll(pessoaDto.enderecos().stream().map(dto -> new Endereco(dto, this)).toList());
    }

    public void updatePessoa(PessoaUpdate pessoaUpdate){
        if(!isNullOrEmpty(pessoaUpdate.nome())) this.nome = pessoaUpdate.nome();
        if(!isNull(pessoaUpdate.nascimento())) this.nascimento = pessoaUpdate.nascimento();
        if(!isNullOrEmpty(pessoaUpdate.telefone())) this.telefone = pessoaUpdate.telefone();
    }
}
