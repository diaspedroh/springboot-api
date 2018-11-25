package com.mbamobi.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbamobi.api.domain.Aplicativo;
import com.mbamobi.api.dto.AplicativoDTO;
import com.mbamobi.api.resources.utils.URL;
import com.mbamobi.api.services.AplicativoService;

@RestController
@RequestMapping(value="/aplicativos")
public class AplicativoResource {
	
	@Autowired
	private AplicativoService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Aplicativo> find(@PathVariable Integer id) {
		Aplicativo obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(value="/page/nome", method=RequestMethod.GET)
	public ResponseEntity<Page<AplicativoDTO>> findPage(
			@RequestParam(value="nome", defaultValue="") String nome, 
			@RequestParam(value="pessoas", defaultValue="") String pessoas, 
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(pessoas);
		Page<Aplicativo> list = service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		Page<AplicativoDTO> listDto = list.map(obj -> new AplicativoDTO(obj));  
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(value="/page/pessoas", method=RequestMethod.GET)
	public ResponseEntity<Page<Aplicativo>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Aplicativo> list = service.search(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}

}
