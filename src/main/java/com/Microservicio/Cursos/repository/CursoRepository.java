package com.Microservicio.Cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Microservicio.Cursos.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}