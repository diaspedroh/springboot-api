package com.mbamobi.api.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.mbamobi.api.domain.Pessoa;
import com.mbamobi.api.dto.PessoaNewDTO;
import com.mbamobi.api.repositories.PessoaRepository;
import com.mbamobi.api.resources.exception.FieldMessage;
import com.mbamobi.api.services.validation.utils.BR;

public class PessoaInsertValidator implements ConstraintValidator<PessoaInsert, PessoaNewDTO> {

	@Autowired
	private PessoaRepository repo;
	
	@Override
	public void initialize(PessoaInsert ann) {
	}

	@Override
	public boolean isValid(PessoaNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if (!BR.isValidCPF(objDto.getCpf())) {
			list.add(new FieldMessage("cpf", "CPF inválido"));
		}
		
		Pessoa aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}

