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

import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.service.EstudianteService;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<Estudiante>> listarEstudiante() {
        return new ResponseEntity<>(estudianteService.listarEstudiantes(), HttpStatus.OK);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<Estudiante> obtenerEstudiantePorRut(@PathVariable String rut) {
        return new ResponseEntity<>(estudianteService.obtenerEstudiantePorRut(rut), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        return new ResponseEntity<>(estudianteService.crearEstudiante(estudiante), HttpStatus.CREATED);
    }

    @PutMapping("/{rut}")
    public ResponseEntity<Estudiante> actualizarEstudiante(
            @PathVariable String rut,
            @RequestBody Estudiante estudianteActualizado) {

        Estudiante estudiante = estudianteService.actualizarEstudiante(rut, estudianteActualizado);
        return ResponseEntity.ok(estudiante);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable String rut)
    {
        estudianteService.eliminarEstudiante(rut);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
