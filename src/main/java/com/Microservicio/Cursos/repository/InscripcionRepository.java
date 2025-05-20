package com.Microservicio.Cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Microservicio.Cursos.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer>
{
    boolean existByCursoAndEstudianteRut(int idCurso, String rut);
    long contByCursoIdCursoAndActivaTrue(int idCurso);
}
