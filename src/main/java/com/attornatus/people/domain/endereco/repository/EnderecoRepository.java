package com.attornatus.people.domain.endereco.repository;

import com.attornatus.people.domain.endereco.Endereco;
import com.attornatus.people.domain.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {

    @Modifying
    @Query("""
    update Endereco e
    set e.principal = false
    where e.pessoa.id = :pessoaId
""")
    void updatePrincipalToFalseByPessoa(Long pessoaId);

    Optional<Endereco> findByCepAndNumeroIgnoreCaseAndPessoa(String cep, String numero, Pessoa pessoa);

}
