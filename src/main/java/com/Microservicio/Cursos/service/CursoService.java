package com.Microservicio.Cursos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.repository.CursoRepository;

import java.util.List;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    public Curso crearCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public void actualizarCupoDisponible(Long idCurso, int cambio) {
        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        if (curso != null) {
            curso.setCupoDisponible(curso.getCupoDisponible() + cambio);
            cursoRepository.save(curso);
        }
    }
}