package com.Microservicio.Cursos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Microservicio.Cursos.model.Inscripcion;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long>{
    Optional<Inscripcion> findByIdUsuarioAndIdCurso(int idUsuario, Long idCurso);
}
