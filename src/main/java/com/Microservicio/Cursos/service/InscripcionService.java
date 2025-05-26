package com.Microservicio.Cursos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.Microservicio.Cursos.model.*;
import com.Microservicio.Cursos.repository.InscripcionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InscripcionService {
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private RestTemplate restTemplate;

    public Inscripcion crearInscripcion(Inscripcion inscripcion) {
        // Verificar si el curso existe
        Curso curso = cursoService.obtenerCursoPorId(inscripcion.getIdCurso());
        if (curso == null) {
            return null;
        }
        
        // Verificar cupos disponibles
        if (curso.getCupoDisponible() <= 0) {
            return null;
        }
        
        // Obtener información del estudiante
        String url = "http://localhost:8081/api/usuarios/" + inscripcion.getIdEstudiante();
        UsuarioDTO estudiante = restTemplate.getForObject(url, UsuarioDTO.class);
        
        if (estudiante == null || !"ESTUDIANTE".equals(estudiante.getTipo())) {
            return null;
        }
        
        inscripcion.setNombreEstudiante(estudiante.getNombre());
        inscripcion.setEstado("PENDIENTE");
        inscripcion.setFechaInscripcion(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        
        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion cambiarEstadoInscripcion(Long idInscripcion, String nuevoEstado) {
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion).orElse(null);
        if (inscripcion == null) {
            return null;
        }
        
        if ("ACEPTADO".equals(nuevoEstado)) {
            cursoService.actualizarCupoDisponible(inscripcion.getIdCurso(), -1);
        }
        
        inscripcion.setEstado(nuevoEstado);
        return inscripcionRepository.save(inscripcion);
    }
}