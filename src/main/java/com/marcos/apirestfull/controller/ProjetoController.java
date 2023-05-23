package com.marcos.apirestfull.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
			projeto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).getOneProjeto(id)).withSelfRel());
		}
		
		return new ResponseEntity<List<ProjetoModel>>(listaProjetos, HttpStatus.OK);
		
	}
	
	@GetMapping("/projetos/{id}")
	public ResponseEntity<ProjetoModel> getOneProjeto(@PathVariable Long id){
		Optional<ProjetoModel> projeto = projetoRepository.findById(id);
		if(!projeto.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		projeto.get().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).getAllProjetos()).withRel("Lista de projetos"));
		return new ResponseEntity<ProjetoModel>(projeto.get(), HttpStatus.OK);
	}
	
	@PostMapping("/projetos")
	public ResponseEntity<ProjetoModel> saveProjeto(@RequestBody ProjetoModel projetoModel){
		ProjetoModel projetoSalvo = projetoRepository.save(projetoModel);
		projetoSalvo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).getAllProjetos()).withRel("Lista de projetos"));
		return new ResponseEntity<ProjetoModel>(projetoSalvo, HttpStatus.OK);
	}
	
	@PutMapping("/projetos/{id}")
	public ResponseEntity<ProjetoModel> replaceProjeto(@RequestBody ProjetoModel projetoModel, @PathVariable Long id){
		Optional<ProjetoModel> projeto = projetoRepository.findById(id);
		if(!projeto.isPresent()) {
			projetoModel.setId(id);
			ProjetoModel projetoSalvo = projetoRepository.save(projetoModel);
			projetoSalvo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).getAllProjetos()).withRel("Lista de projetos"));
			return new ResponseEntity<ProjetoModel>(projetoSalvo, HttpStatus.OK);
		}
		ProjetoModel projetoUpdate = projeto.get();
			projetoUpdate.setNome(projetoModel.getNome());
			projetoUpdate.setDescricao(projetoModel.getDescricao());
			projetoUpdate.setUrl(projetoModel.getUrl().toString());
			projetoUpdate.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).getAllProjetos()).withRel("Lista de projetos"));
			return new ResponseEntity<ProjetoModel>(projetoRepository.save(projeto.get()), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/projetos/{id}")
	public ResponseEntity<List<ProjetoModel>>deleteProjeto(@PathVariable Long id){
		return new ResponseEntity<List<ProjetoModel>>(HttpStatus.NO_CONTENT);
	}
}
	
