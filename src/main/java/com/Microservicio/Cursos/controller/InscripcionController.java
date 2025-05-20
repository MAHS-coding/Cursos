package com.Microservicio.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Microservicio.Cursos.exception.CupoAgotadoException;
import com.Microservicio.Cursos.exception.CursoNotFoundException;
import com.Microservicio.Cursos.exception.EstudianteNotFoundException;
import com.Microservicio.Cursos.exception.EstudianteYaInscritoException;
import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.service.InscripcionService;

@RestController
@RequestMapping("api/inscripciones")
public class InscripcionController {
    
    @Autowired
    private InscripcionService inscripcionService;

    @PostMapping("/curso/{idCurso}/estudiante/{rut}")
    public ResponseEntity<?> inscribirEstudiante(
            @PathVariable int idCurso,
            @PathVariable String rut) {
        try {
            Inscripcion resultado = inscripcionService.inscribirEstudiante(idCurso, rut);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CursoNotFoundException | EstudianteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CupoAgotadoException | EstudianteYaInscritoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/curso/{idCurso}/estudiantes")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesInscritos(
            @PathVariable int idCurso) {
        return ResponseEntity.ok(inscripcionService.obtenerEstudiantesInscritos(idCurso));
    }

    // Otros endpoints relacionados con inscripciones
}