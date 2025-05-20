package com.Microservicio.Cursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Microservicio.Cursos.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer>
{
    List<Inscripcion> findByCursoIdCurso(int idCurso);
    boolean existByCursoAndEstudianteRut(int idCurso, String rut);
    long contByCursoIdCursoAndActivaTrue(int idCurso);
}
