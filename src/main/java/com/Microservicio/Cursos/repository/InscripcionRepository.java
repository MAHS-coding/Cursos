package com.Microservicio.Cursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Microservicio.Cursos.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    List<Inscripcion> findByCursoIdCurso(int idCurso);
    boolean existsByCursoIdCursoAndEstudianteRut(int idCurso, String rut); // Cambiado para buscar por ID
    long countByCursoIdCursoAndActivaTrue(int idCurso); // Corregido "count"
}
