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

    @Autowired
  

    public List<Curso> listarCursos()
    {
        return cursoRepository.findAll();
    }

    public Curso crearCurso(Curso curso)
    {
        if(curso.getCupoMaximo() <= 0) {
            throw new IllegalArgumentException("El cupo maximo debe ser mayor a 0");
        }
        curso.setCupoDisponible(curso.getCupoMaximo());
        return cursoRepository.save(curso);
    }

}