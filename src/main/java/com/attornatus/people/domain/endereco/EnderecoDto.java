package com.attornatus.people.domain.endereco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;
import com.attornatus.people.domain.endereco.validation.PeopleCep;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"classInfo", "fieldInfos", "unknownKeys","factory","empty"})
public class EnderecoDto extends GenericJson {
    @Key @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String logradouro;
    @Key @PeopleCep
    private String cep;
    @Key @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String complemento;
    @Key @JsonProperty(value = "cidade", access = JsonProperty.Access.READ_ONLY)
    private  String localidade;
    @Key @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String bairro;
    @Key @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String uf;
    @Key @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ddd;
    @Key @NotEmpty @Size(min = 1, max = 10)
    private String numero;
    @Key @NotNull
    private Boolean principal;

    public EnderecoDto(Endereco endereco){
        this(endereco.getLogradouro(),
                endereco.getCep(),
                endereco.getComplemento(),
                endereco.getLocalidade(),
                endereco.getBairro(),
                endereco.getUf(),
                endereco.getDdd(),
                endereco.getNumero(),
                endereco.getPrincipal());
    }

    public EnderecoDto set(String fieldName, Object value) {return (EnderecoDto) super.set(fieldName, value);}

    public EnderecoDto clone() {
        return (EnderecoDto)super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EnderecoDto that = (EnderecoDto) o;
        return Objects.equals(cep, that.cep) && Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cep, numero);
    }
}
