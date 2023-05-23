package com.marcos.apirestfull.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.apirestfull.model.ProjetoModel;
import com.marcos.apirestfull.repository.ProjetoRepository;
import lombok.*;

@RestController
public class ProjetoController {

	@Autowired
	ProjetoRepository projetoRepository;
	
	@GetMapping("/projetos")
	public ResponseEntity<List<ProjetoModel>> getAllProjetos(){
		List<ProjetoModel> listaProjetos = projetoRepository.findAll();
		if(listaProjetos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		for(ProjetoModel projeto : listaProjetos) {
			Long id = projeto.getId();
			projeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).getOneProduto(id)).withSelfRel());
		}
		
		return new ResponseEntity<List<ProjetoModel>>(listaProjetos, HttpStatus.OK);
		
	}
	
	@GetMapping("produtos/{id}")
	public ResponseEntity<ProjetoModel> getOneProduto(@PathVariable(value="id") Long id){
		Optional<ProjetoModel> projeto = projetoRepository.findById(id);
		if(!projeto.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		projeto.get().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).getOneProduto(id)).withRel("Lista de projetos"));
		return new ResponseEntity<ProjetoModel>(projeto.get(), HttpStatus.OK);
	}
}
