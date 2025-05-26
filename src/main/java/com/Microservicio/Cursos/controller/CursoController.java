package com.Microservicio.Cursos.controller;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired private CursoService cursoService;

    @PostMapping
    public ResponseEntity<Curso> crearCurso(
            @RequestBody Curso curso,
            @RequestParam Integer profesorId) {
        return ResponseEntity.ok(cursoService.crearCurso(curso, profesorId));
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Curso>> listarCursosActivos() {
        return ResponseEntity.ok(cursoService.listarCursosActivos());
    }

    @GetMapping("/profesor/{idProfesor}")
    public ResponseEntity<List<Curso>> listarCursosPorProfesor(@PathVariable Integer idProfesor) {
        return ResponseEntity.ok(cursoService.listarCursosPorProfesor(idProfesor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerCurso(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerCursoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(
            @PathVariable Long id,
            @RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.actualizarCurso(id, curso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}