package com.Microservicio.Cursos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping("/inscribirse")
    public ResponseEntity<Inscripcion> postInscripcion(@RequestBody Inscripcion inscripcion) {
        try {
            return new ResponseEntity<>(inscripcionService.crearInscripcion(inscripcion), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{idInscripcion}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long idInscripcion,
            @RequestBody Map<String, String> request) {

        EstadoInscripcion estado = EstadoInscripcion.valueOf(request.get("estado"));
        Inscripcion resultado = inscripcionService.cambiarEstado(idInscripcion, estado);

        if (estado == EstadoInscripcion.RECHAZADO) {
            return ResponseEntity.ok("✅ Inscripción rechazada y eliminada correctamente.");
        } else if (estado == EstadoInscripcion.ACEPTADO) {
            return ResponseEntity.ok("✅ Inscripción aceptada. El usuario ha sido inscrito en el curso.");
        } else {
            return ResponseEntity.ok(resultado);
        }
    }

}
