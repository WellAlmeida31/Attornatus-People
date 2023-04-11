package com.attornatus.people.domain.pessoa;

import com.attornatus.people.domain.endereco.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa,Long> {

    Page<Pessoa> findAll(Pageable pageable);
    @Override
    Optional<Pessoa> findById(Long aLong);

    @Query("""
    select p.enderecos from Pessoa p
    where p.id = :id
""")
    List<Endereco> findAllEnderecosByPessoaId(Long id);

    @Query("""
    select p.enderecos from Pessoa p
    inner join p.enderecos e
    where p.id = :id
    and
    e.principal = true
""")
    Optional<Endereco> findEnderecoPrincipalByPessoaId(Long id);

    @Query("""
    select p from Pessoa p
    inner join p.enderecos e
    where p.nome = :nome
    and
    p.nascimento = :nascimento
    and
    e.cep in :cep
    and
    e.numero in :numero
""")
    List<Pessoa> existsByPessoaAndEnderecos(String nome, LocalDate nascimento, List<String> cep, List<String> numero);

}
