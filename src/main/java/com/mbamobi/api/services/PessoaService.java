package com.mbamobi.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbamobi.api.domain.Cidade;
import com.mbamobi.api.domain.Endereco;
import com.mbamobi.api.domain.Pessoa;
import com.mbamobi.api.domain.enums.Perfil;
import com.mbamobi.api.dto.PessoaDTO;
import com.mbamobi.api.dto.PessoaNewDTO;
import com.mbamobi.api.repositories.EnderecoRepository;
import com.mbamobi.api.repositories.PessoaRepository;
import com.mbamobi.api.security.UserSS;
import com.mbamobi.api.services.exceptions.AuthorizationException;
import com.mbamobi.api.services.exceptions.DataIntegrityException;
import com.mbamobi.api.services.exceptions.ObjectNotFoundException;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	public Pessoa find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) || !user.hasRole(Perfil.GESTOR) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Pessoa> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pessoa.class.getName()));
	}
	
	@Transactional
	public Pessoa insert(Pessoa obj) {
		obj.setId(null);
		obj = repository.save(obj);
		enderecoRepository.save(obj.getEndereco());
		return obj;
	}
	
	public Pessoa update(Pessoa obj) {
		Pessoa newObj = find(obj.getId());
		updateData(newObj, obj);
		return repository.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há aplicativos associados");
		}
	}
	
	public List<Pessoa> findAll() {
		return repository.findAll();
	}
	
	public Pessoa findByEmail(String email) {
		UserSS user = UserService.authenticated();

		Pessoa obj = repository.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Pessoa.class.getName());
		}
		return obj;
	}
	
	public Page<Pessoa> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Pessoa fromDTO(PessoaDTO objDto) {
		return new Pessoa(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Pessoa fromDTO(PessoaNewDTO objDto) {
		Pessoa pes = new Pessoa(null, objDto.getNome(), objDto.getEmail(), objDto.getCpf(), objDto.getRg(), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), pes, cid);
		pes.setEndereco(end);
		pes.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2()!=null) {
			pes.getTelefones().add(objDto.getTelefone2());
		}
		return pes;
	}
	
	private void updateData(Pessoa newObj, Pessoa obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
