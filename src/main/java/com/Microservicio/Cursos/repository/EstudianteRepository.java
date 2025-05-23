package com.Microservicio.Cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Microservicio.Cursos.model.Estudiante;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String>{
    
}
