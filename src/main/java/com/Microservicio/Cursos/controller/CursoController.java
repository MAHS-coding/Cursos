package com.Microservicio.Cursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.service.CursoService;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> getCursos() {
        List<Curso> cursos = cursoService.listarCursos();
        if (cursos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Curso getCursoPorId(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id);
    }

    @PostMapping
    public ResponseEntity<Curso> postCurso(@RequestBody Curso curso) {
        try {
            return new ResponseEntity<>(cursoService.crearCurso(curso), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}