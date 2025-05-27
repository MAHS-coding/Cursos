package com.Microservicio.Cursos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Microservicio.Cursos.model.Curso;

import com.Microservicio.Cursos.repository.CursoRepository;

import java.util.List;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso obtenerCursoPorId(Long idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

    }

    public Curso crearCurso(Curso curso) {
        if (curso.getCupoMaximo() <= 0) {
            throw new IllegalArgumentException("El cupo maximo debe ser mayor a 0");
        }
        curso.setCupoDisponible(curso.getCupoMaximo());
        return cursoRepository.save(curso);
    }

}