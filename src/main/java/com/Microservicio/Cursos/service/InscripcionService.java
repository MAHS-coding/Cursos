package com.Microservicio.Cursos.service;

import com.Microservicio.Cursos.model.*;
import com.Microservicio.Cursos.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InscripcionService {
    @Autowired private InscripcionRepository inscripcionRepository;
    @Autowired private RestTemplate restTemplate;
    @Autowired private CursoService cursoService;

    public Inscripcion solicitarInscripcion(Integer idEstudiante, Long idCurso) {
        UsuarioDTO estudiante = obtenerUsuario(idEstudiante);
        if (estudiante == null || !estudiante.isActivo() || !"ESTUDIANTE".equals(estudiante.getTipoUsuario())) {
            throw new RuntimeException("El usuario no es un estudiante válido o está inactivo");
        }

        Curso curso = cursoService.obtenerCursoPorId(idCurso);
        if (!curso.isActivo()) {
            throw new RuntimeException("El curso no está activo");
        }

        if (inscripcionRepository.existsByIdEstudianteAndIdCurso(idEstudiante, idCurso)) {
            throw new RuntimeException("El estudiante ya tiene una solicitud para este curso");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdCurso(idCurso);
        inscripcion.setIdEstudiante(idEstudiante);
        inscripcion.setEstado(EstadoInscripcion.PENDIENTE);

        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion aprobarInscripcion(Long idInscripcion, Integer idAdmin) {
        UsuarioDTO admin = obtenerUsuario(idAdmin);
        if (admin == null || !admin.isActivo() || !"ADMINISTRADOR".equals(admin.getTipoUsuario())) {
            throw new RuntimeException("No tiene permisos para aprobar inscripciones");
        }

        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        if (inscripcion.getEstado() != EstadoInscripcion.PENDIENTE) {
            throw new RuntimeException("Solo se pueden aprobar inscripciones pendientes");
        }

        Curso curso = cursoService.obtenerCursoPorId(inscripcion.getIdCurso());
        if (curso.getCupoDisponible() <= 0) {
            throw new RuntimeException("No hay cupo disponible en el curso");
        }

        curso.setCupoDisponible(curso.getCupoDisponible() - 1);
        cursoService.actualizarCurso(curso.getId(), curso);

        inscripcion.setEstado(EstadoInscripcion.APROBADA);
        return inscripcionRepository.save(inscripcion);
    }

    public List<Inscripcion> listarInscripcionesPorCurso(Long idCurso) {
        return inscripcionRepository.findByIdCurso(idCurso);
    }

    public List<Inscripcion> listarInscripcionesPendientesPorCurso(Long idCurso) {
        return inscripcionRepository.findByIdCursoAndEstado(idCurso, EstadoInscripcion.PENDIENTE);
    }

    private UsuarioDTO obtenerUsuario(Integer idUsuario) {
        String url = "http://localhost:8081/api/usuarios/public/" + idUsuario;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
}