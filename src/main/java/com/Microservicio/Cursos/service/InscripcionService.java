package com.Microservicio.Cursos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.model.EstadoInscripcion;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.model.UsuarioDTO;
import com.Microservicio.Cursos.repository.CursoRepository;
import com.Microservicio.Cursos.repository.InscripcionRepository;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Mostrar todas las inscripciones
    public List<Inscripcion> listarInscripciones() {
        return inscripcionRepository.findAll();
    }

    public Inscripcion crearInscripcion(Inscripcion inscripcion) {
        // 1. Validar existencia del usuario
        String urlUsuario = "http://localhost:8083/api/usuarios/" + inscripcion.getIdUsuario();
        UsuarioDTO usuario;
        try {
            usuario = restTemplate.getForObject(urlUsuario, UsuarioDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar usuario");
        }

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        if (usuario.getTipoUsuario() == null || !"ESTUDIANTE".equalsIgnoreCase(usuario.getTipoUsuario())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo estudiantes pueden inscribirse a un curso");
        }

        Optional<Inscripcion> existente = inscripcionRepository.findByIdUsuarioAndIdCurso(inscripcion.getIdUsuario(),
                inscripcion.getIdCurso());
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una inscripción para este curso");
        }

        // 3. Guardar inscripción
        inscripcion.setEstado(EstadoInscripcion.PENDIENTE); // por si no viene del frontend
        Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);

        return inscripcionGuardada;
    }

    // Cambiar el estado de una inscripcion
    // url =
    public Inscripcion cambiarEstado(Long idInscripcion, EstadoInscripcion nuevoEstado) {
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscripción no encontrada"));

        Curso curso = cursoRepository.findById(inscripcion.getIdCurso())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        if (nuevoEstado == EstadoInscripcion.ACEPTADO) {
            if (curso.getCupoDisponible() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay cupos disponibles");
            }

            curso.setCupoDisponible(curso.getCupoDisponible() - 1);
            cursoRepository.save(curso);

            // Vincular curso al usuario
            String urlUsuario = "http://localhost:8083/api/usuarios/" + inscripcion.getIdUsuario() + "/vincular-curso";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("cursoId", curso.getIdCurso());
            requestBody.put("cursoNombre", curso.getNombreCurso());

            restTemplate.put(urlUsuario, requestBody);

            // Eliminar la inscripción
            inscripcionRepository.delete(inscripcion);
            return null; // No se devuelve entidad, solo mensaje
        }

        if (nuevoEstado == EstadoInscripcion.RECHAZADO) {
            inscripcionRepository.delete(inscripcion);
            return null;
        }

        // Si no es aceptada ni rechazada, se actualiza el estado
        inscripcion.setEstado(nuevoEstado);
        return inscripcionRepository.save(inscripcion);
    }
}
