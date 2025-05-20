package com.Microservicio.Cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Microservicio.Cursos.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, String>
{
    boolean existsByRut(String rut);
}
