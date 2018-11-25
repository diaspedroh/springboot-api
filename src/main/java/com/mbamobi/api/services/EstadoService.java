package com.mbamobi.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbamobi.api.domain.Estado;
import com.mbamobi.api.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repository;
	
	public List<Estado> findAll() {
		return repository.findAllByOrderByNome();
	}
}
