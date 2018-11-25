package com.mbamobi.api.services;

import java.text.ParseException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbamobi.api.domain.Aplicativo;
import com.mbamobi.api.domain.Cidade;
import com.mbamobi.api.domain.Endereco;
import com.mbamobi.api.domain.Estado;
import com.mbamobi.api.domain.Pessoa;
import com.mbamobi.api.domain.enums.Perfil;
import com.mbamobi.api.repositories.AplicativoRepository;
import com.mbamobi.api.repositories.CidadeRepository;
import com.mbamobi.api.repositories.EnderecoRepository;
import com.mbamobi.api.repositories.EstadoRepository;
import com.mbamobi.api.repositories.PessoaRepository;

@Service
public class DBService {

	@Autowired
	private BCryptPasswordEncoder pe;
	@Autowired
	private AplicativoRepository aplicativoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public void instantiateTestDatabase() throws ParseException {
		
		Estado est1 = new Estado(null, "Distrito Federal");
		Estado est2 = new Estado(null, "Goiás");
		
		Cidade c1 = new Cidade(null, "Brasília", est1);
		Cidade c2 = new Cidade(null, "Anápolis", est2);
		Cidade c3 = new Cidade(null, "Goiânia", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Aplicativo ap1 = new Aplicativo(null, "Facebook");
		Aplicativo ap2 = new Aplicativo(null, "Instagram");
		Aplicativo ap3 = new Aplicativo(null, "Twitter");
		Aplicativo ap4 = new Aplicativo(null, "Spotify");
		Aplicativo ap5 = new Aplicativo(null, "WhatsApp");;
		
		Pessoa pes1 = new Pessoa(null, "José Maria", "josemaria@gmail.com", "57531993058", "1111111/DF", pe.encode("pessoa1"));
		pes1.getTelefones().addAll(Arrays.asList("33333333", "998888888"));
		Endereco e1 = new Endereco(null, "SQN", "104", "N/A", "Asa Norte", "71000000", pes1, c1);
		pes1.setEndereco(e1);
		
		Pessoa pes2 = new Pessoa(null, "Maria José", "mariajose@gmail.com", "65466466093", "2222222", pe.encode("pessoa2"));
		pes2.getTelefones().addAll(Arrays.asList("34444444", "997777777"));
		pes2.addPerfil(Perfil.ADMIN);
		Endereco e2 = new Endereco(null, "Setor Tradicional", "500", "Lote 120/130", "Jundiaí", "61000000/GO", pes2, c2);
		pes2.setEndereco(e2);
		
		Pessoa pes3 = new Pessoa(null, "Paulo Silva", "paulosilva@gmail.com", "17325446039", "3333333/GO", pe.encode("pessoa3"));
		pes3.getTelefones().addAll(Arrays.asList("35555555", "996666666"));
		pes3.addPerfil(Perfil.GESTOR);
		Endereco e3 = new Endereco(null, "Jardim Ipanema", "1040", null, "Setor Bueno", "62000000", pes3, c3);
		pes3.setEndereco(e3);

		ap1.getPessoas().addAll(Arrays.asList(pes1, pes2));
		ap2.getPessoas().addAll(Arrays.asList(pes2, pes3));
		ap3.getPessoas().addAll(Arrays.asList(pes1, pes3));
		ap4.getPessoas().addAll(Arrays.asList(pes1, pes2, pes3));
		ap5.getPessoas().addAll(Arrays.asList(pes1, pes2, pes3));
		
		pes1.getAplicativos().addAll(Arrays.asList(ap1, ap3, ap4, ap5));
		pes2.getAplicativos().addAll(Arrays.asList(ap1, ap2, ap4, ap5));
		pes3.getAplicativos().addAll(Arrays.asList(ap2, ap3, ap4, ap5));

		pessoaRepository.saveAll(Arrays.asList(pes1, pes2, pes3));
		aplicativoRepository.saveAll(Arrays.asList(ap1, ap2, ap3, ap3, ap4, ap5));
		enderecoRepository.saveAll(Arrays.asList(e1, e2, e3));
		
	}
}
