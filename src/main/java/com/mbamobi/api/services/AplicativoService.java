package com.mbamobi.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mbamobi.api.domain.Aplicativo;
import com.mbamobi.api.domain.Pessoa;
import com.mbamobi.api.repositories.AplicativoRepository;
import com.mbamobi.api.repositories.PessoaRepository;
import com.mbamobi.api.security.UserSS;
import com.mbamobi.api.services.exceptions.AuthorizationException;
import com.mbamobi.api.services.exceptions.ObjectNotFoundException;

@Service
public class AplicativoService {
	
	@Autowired
	private AplicativoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Aplicativo find(Integer id) {
		Optional<Aplicativo> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Aplicativo.class.getName()));
	}

	public Page<Aplicativo> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Pessoa> pessoas = pessoaRepository.findAllById(ids);
		return repository.findDistinctByNomeContainingAndPessoasIn(nome, pessoas, pageRequest);	
	}
	
	public Page<Aplicativo> search(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Pessoa object = pessoaRepository.findById(user.getId()).get();
		return repository.findByPessoaId(object.getId(), pageRequest);	
	}
}
