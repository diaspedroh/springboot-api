package com.mbamobi.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mbamobi.api.domain.Pessoa;
import com.mbamobi.api.repositories.PessoaRepository;
import com.mbamobi.api.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PessoaRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Pessoa pes = repository.findByEmail(email);
		if (pes == null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(pes.getId(), pes.getEmail(), pes.getSenha(), pes.getPerfis());
	}
}
