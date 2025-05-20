package com.Microservicio.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Microservicio.Cursos.exception.CursoNotFoundException;
import com.Microservicio.Cursos.model.Curso;

import com.Microservicio.Cursos.service.CursoService;

@RestController
@RequestMapping("api/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> getCursos() {
        return new ResponseEntity<>(cursoService.listarTodosCursos(), HttpStatus.OK);
    }

    @GetMapping("/{idCurso}")
    public ResponseEntity<?> getCursoPorId(@PathVariable int idCurso) {
        try {
            Curso curso = cursoService.buscarCursoPorId(idCurso);
            return ResponseEntity.ok(curso);
        } catch (CursoNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Curso> postCurso(@RequestBody Curso curso) {
        return new ResponseEntity<>(cursoService.crearCurso(curso), HttpStatus.OK);
    }

    @PutMapping("/{idCurso}")
    public ResponseEntity<?> actualizarCurso(
            @PathVariable int idCurso,
            @RequestBody Curso cursoActualizado) {
        try {
            Curso curso = cursoService.actualizarCurso(idCurso, cursoActualizado);
            return ResponseEntity.ok(curso);
        } catch (CursoNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
}
