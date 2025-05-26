package com.Microservicio.Cursos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.service.InscripcionService;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {
    @Autowired
    private InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<Inscripcion> crearInscripcion(@RequestBody Inscripcion inscripcion) {
        Inscripcion nuevaInscripcion = inscripcionService.crearInscripcion(inscripcion);
        if (nuevaInscripcion == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(nuevaInscripcion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Inscripcion> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        Inscripcion inscripcion = inscripcionService.cambiarEstadoInscripcion(id, estado);
        if (inscripcion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscripcion, HttpStatus.OK);
    }
}