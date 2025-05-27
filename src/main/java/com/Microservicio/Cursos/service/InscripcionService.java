package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.Microservicio.Cursos.model.EstadoInscripcion;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.model.UsuarioDTO;
import com.Microservicio.Cursos.repository.InscripcionRepository;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Mostrar todas las inscripciones
    public List<Inscripcion> listarInscripciones() {
        return inscripcionRepository.findAll();
    }

    public Inscripcion crearInscripcion(Inscripcion inscripcion) {
        String url = "http://localhost:8080/api/usuarios/" + inscripcion.getIdUsuario();
        UsuarioDTO usuario = restTemplate.getForObject(url, UsuarioDTO.class);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        inscripcion.setIdUsuario(usuario.getIdUsuario());
        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion cambiarEstado(Long idInscripcion, EstadoInscripcion nuevoEstado) {
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Inscripción no encontrada con ID: " + idInscripcion));

        if (nuevoEstado == EstadoInscripcion.RECHAZADO) {
            // Elimina la inscripción si es RECHAZADO
            inscripcionRepository.delete(inscripcion);
            return inscripcion; // Retorna la inscripción eliminada
        } else {
            // Actualiza el estado si es ACEPTADO o PENDIENTE
            inscripcion.setEstado(nuevoEstado);
            return inscripcionRepository.save(inscripcion);
        }
    }
}
