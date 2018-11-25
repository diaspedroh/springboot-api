package com.mbamobi.api.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mbamobi.api.domain.Aplicativo;
import com.mbamobi.api.domain.Pessoa;

@Repository
public interface AplicativoRepository extends JpaRepository<Aplicativo, Integer> {

	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Aplicativo obj INNER JOIN obj.pessoas cat WHERE obj.nome LIKE %:nome% AND cat IN :pessoas")
	Page<Aplicativo> findDistinctByNomeContainingAndPessoasIn(@Param("nome") String nome, @Param("pessoas") List<Pessoa> pessoas, Pageable pageRequest);
	
	@Transactional(readOnly=true)
	@Query("SELECT obj FROM Aplicativo obj INNER JOIN obj.pessoas pes WHERE pes.id = :pessoa")
	Page<Aplicativo> findByPessoaId(Integer pessoa, Pageable pageRequest);
}
