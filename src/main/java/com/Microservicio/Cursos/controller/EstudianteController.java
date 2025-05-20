package com.Microservicio.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.service.EstudianteService;

@RestController
@RequestMapping("api/estudiantes")
public class EstudianteController {
    
    @Autowired
    private EstudianteService estudianteService;

    @PostMapping
    public ResponseEntity<?> crearEstudiante(@RequestBody Estudiante estudiante) {
        try {
            Estudiante estudianteGuardado = estudianteService.crearEstudiante(estudiante);
            return ResponseEntity.ok(estudianteGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> listarEstudiantes() {
        return ResponseEntity.ok(estudianteService.listarTodosEstudiantes());
    }
}
