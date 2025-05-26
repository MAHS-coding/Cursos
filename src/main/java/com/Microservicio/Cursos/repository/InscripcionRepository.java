package com.Microservicio.Cursos.repository;

import com.Microservicio.Cursos.model.EstadoInscripcion;
import com.Microservicio.Cursos.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByIdEstudiante(Integer idEstudiante);
    List<Inscripcion> findByIdCurso(Long idCurso);
    List<Inscripcion> findByIdCursoAndEstado(Long idCurso, EstadoInscripcion estado);
    boolean existsByIdEstudianteAndIdCurso(Integer idEstudiante, Long idCurso);
}