package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.repository.CursoRepository;

public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listarCursos()
    {
        return cursoRepository.findAll();
    }

    public Curso guardarCurso(Curso curso)
    {
        return cursoRepository.save(curso);
    }
}
