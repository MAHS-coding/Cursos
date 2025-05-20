package com.Microservicio.Cursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.service.CursoService;

@RestController
@RequestMapping("api/cursos")
public class CursoController
{
    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<Curso> getCursos()
    {
        return new ResponseEntity<>(cursoService.listarCursos(),HttpStatus.OK);
    }
}