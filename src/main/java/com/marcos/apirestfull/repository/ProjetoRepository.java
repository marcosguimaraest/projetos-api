package com.marcos.apirestfull.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcos.apirestfull.model.ProjetoModel;

public interface ProjetoRepository extends JpaRepository<ProjetoModel, Long> {

}
