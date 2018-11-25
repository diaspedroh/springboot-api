package com.mbamobi.api.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.mbamobi.api.domain.Pessoa;
import com.mbamobi.api.dto.PessoaDTO;
import com.mbamobi.api.repositories.PessoaRepository;
import com.mbamobi.api.resources.exception.FieldMessage;

public class PessoaUpdateValidator implements ConstraintValidator<PessoaUpdate, PessoaDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PessoaRepository repo;
	
	@Override
	public void initialize(PessoaUpdate ann) {
	}

	@Override
	public boolean isValid(PessoaDTO objDto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		Pessoa aux = repo.findByEmail(objDto.getEmail());
		if (aux != null && !aux.getId().equals(uriId)) {
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

