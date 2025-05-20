package com.Microservicio.Cursos.repository;

import com.Microservicio.Cursos.model.Inscripcion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
    List<Inscripcion> findByCursoIdCurso(int idCurso);

    boolean existsByCursoIdCursoAndEstudianteRut(int idCurso, String rut);

    long countByCursoIdCursoAndActivaTrue(int idCurso);

    // ✅ Estos son los métodos que faltan
    List<Inscripcion> findByCursoIdCursoAndActivaTrue(int idCurso);

    List<Inscripcion> findByEstudianteRutAndActivaTrue(String rut);
}

