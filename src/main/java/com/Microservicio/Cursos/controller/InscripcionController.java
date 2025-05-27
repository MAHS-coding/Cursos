package com.Microservicio.Cursos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.Cursos.model.EstadoInscripcion;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.service.InscripcionService;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @GetMapping
    public ResponseEntity<List<Inscripcion>> getInscripciones() {
        List<Inscripcion> inscripcioes = inscripcionService.listarInscripciones();
        if (inscripcioes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(inscripcionService.listarInscripciones(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Inscripcion> postInscripcion(@RequestBody Inscripcion inscripcion) {
        try {
            return new ResponseEntity<>(inscripcionService.crearInscripcion(inscripcion), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{idInscripcion}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long idInscripcion,
            @RequestBody Map<String, String> request) {

        EstadoInscripcion estado = EstadoInscripcion.valueOf(request.get("estado"));
        Inscripcion inscripcion = inscripcionService.cambiarEstado(idInscripcion, estado);

        if (estado == EstadoInscripcion.RECHAZADO) {
            return ResponseEntity.ok().body("Inscripción rechazada y eliminada correctamente");
        } else {
            return ResponseEntity.ok(inscripcion);
        }
    }
}
