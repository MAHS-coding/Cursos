package com.Microservicio.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.Cursos.exception.CupoAgotadoException;
import com.Microservicio.Cursos.exception.CursoNotFoundException;
import com.Microservicio.Cursos.exception.EstudianteNotFoundException;
import com.Microservicio.Cursos.exception.EstudianteYaInscritoException;
import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.service.CursoService;

@RestController
@RequestMapping("api/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> getCursos() {
        return new ResponseEntity<>(cursoService.listarCursos(), HttpStatus.OK);
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
        return new ResponseEntity<>(cursoService.guardarCurso(curso), HttpStatus.OK);
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

    @PostMapping("/{idCurso}/inscribir")
    public ResponseEntity<?> inscribirEstudiante(
            @PathVariable int idCurso,
            @RequestParam String rut) {
        try {
            String resultado = cursoService.inscribirEstudiante(idCurso, rut);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CursoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EstudianteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CupoAgotadoException | EstudianteYaInscritoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{idCurso}/estudiantes")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesInscritos(
            @PathVariable int idCurso) {
        return ResponseEntity.ok(cursoService.obtenerEstudiantesInscritos(idCurso));
    }

    // Endpoint para crear estudiantes
    @PostMapping("/estudiantes")
    public ResponseEntity<?> crearEstudiante(@RequestBody Estudiante estudiante) {
        try {
            Estudiante estudianteGuardado = cursoService.guardarEstudiante(estudiante);
            return ResponseEntity.ok(estudianteGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
