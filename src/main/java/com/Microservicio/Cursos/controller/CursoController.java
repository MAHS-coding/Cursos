package com.Microservicio.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.service.CursoService;

@RestController
@RequestMapping("api/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;

    // Mostrar los cursos existentes
    @GetMapping
    public ResponseEntity<List<Curso>> getCursos() {
        return new ResponseEntity<>(cursoService.listarCursos(), HttpStatus.OK);
    }

    // Mostrar los cursos existentes por idCurso
    @GetMapping("/{idCurso}")
    public ResponseEntity<Curso> obtenerCursoPorId(@PathVariable int idCurso) {
        Curso curso = cursoService.obtenerCursoPorId(idCurso);
        return new ResponseEntity<>(curso, HttpStatus.OK);
    }

    // Publicar un nuevo curso
    @PostMapping
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        Curso nuevoCurso = cursoService.crearCurso(curso);
        return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED);
    }

    // Modificar un curso por idCurso
    @PutMapping("/{idCurso}")
    public ResponseEntity<Curso> actualizarCurso(
            @PathVariable("idCurso") int idCurso,
            @RequestBody Curso cursoActualizado) {
        cursoActualizado.setIdCurso(idCurso);

        Curso cursoActualizadoDB = cursoService.actualizarCurso(cursoActualizado);
        return new ResponseEntity<>(cursoActualizadoDB, HttpStatus.OK);
    }

    // Asignar profesor a curso
    @PutMapping("/{idCurso}/profesores/{idProfesor}")
    public ResponseEntity<?> asignarProfesor(
            @PathVariable int idCurso,
            @PathVariable int idProfesor) {

        try {
            Curso cursoActualizado = cursoService.asignarProfesorACurso(idCurso, idProfesor);
            return ResponseEntity.ok(cursoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al asignar profesor: " + e.getMessage());
        }
    }

    // Desasociar profesor de un curso
    @DeleteMapping("/{idCurso}/profesor")
    public ResponseEntity<Void> removerProfesor(@PathVariable int idCurso) {
        cursoService.removerProfesorDeCurso(idCurso);
        return ResponseEntity.noContent().build();
    }

    // Eliminar curso por idCurso
    @DeleteMapping("/{idCurso}")
    public ResponseEntity<?> eliminarCurso(@PathVariable int idCurso) {
        try {
            cursoService.eliminarCurso(idCurso);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}