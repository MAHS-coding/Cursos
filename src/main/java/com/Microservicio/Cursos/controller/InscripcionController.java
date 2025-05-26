package com.Microservicio.Cursos.controller;

import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {
    @Autowired private InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<Inscripcion> solicitarInscripcion(
            @RequestParam Integer estudianteId,
            @RequestParam Long cursoId) {
        return ResponseEntity.ok(inscripcionService.solicitarInscripcion(estudianteId, cursoId));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Inscripcion> aprobarInscripcion(
            @PathVariable Long id,
            @RequestParam Integer adminId) {
        return ResponseEntity.ok(inscripcionService.aprobarInscripcion(id, adminId));
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> listarInscripcionesPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(inscripcionService.listarInscripcionesPorCurso(cursoId));
    }

    @GetMapping("/curso/{cursoId}/pendientes")
    public ResponseEntity<List<Inscripcion>> listarInscripcionesPendientes(@PathVariable Long cursoId) {
        return ResponseEntity.ok(inscripcionService.listarInscripcionesPendientesPorCurso(cursoId));
    }
}