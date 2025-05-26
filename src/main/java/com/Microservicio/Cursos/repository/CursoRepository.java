package com.Microservicio.Cursos.repository;

import com.Microservicio.Cursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByIdProfesor(Integer idProfesor);
    List<Curso> findByActivoTrue();
}