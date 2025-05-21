package com.Microservicio.Cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Microservicio.Cursos.model.Profesor;

@Repository
public interface ProfesorRepository  extends JpaRepository<Profesor, Integer>{
    
}