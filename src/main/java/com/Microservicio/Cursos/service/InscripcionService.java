package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.Cursos.exception.CupoAgotadoException;
import com.Microservicio.Cursos.exception.EstudianteYaInscritoException;
import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.repository.InscripcionRepository;
import com.Microservicio.Cursos.utils.RutUtils;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final CursoService cursoService;
    private final EstudianteService estudianteService;
    @Autowired
    public InscripcionService(InscripcionRepository inscripcionRepository,
                            CursoService cursoService,
                            EstudianteService estudianteService,
                            RutUtils rutUtils) {
        this.inscripcionRepository = inscripcionRepository;
        this.cursoService = cursoService;
        this.estudianteService = estudianteService;
    }

    public Inscripcion inscribirEstudiante(int idCurso, String rutEstudiante) {
        validarRut(rutEstudiante);
        String rutFormateado = RutUtils.formatearRut(rutEstudiante);

        Curso curso = cursoService.buscarCursoPorId(idCurso);
        Estudiante estudiante = estudianteService.buscarEstudiantePorRut(rutFormateado);

        validarInscripcion(idCurso, rutFormateado, curso);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setCurso(curso);
        inscripcion.setEstudiante(estudiante);
        inscripcion.setActiva(true);

        Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);
        cursoService.actualizarCuposDisponibles(idCurso, -1);

        return inscripcionGuardada;
    }

    public void desinscribirEstudiante(int idInscripcion) {
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        if (!inscripcion.isActiva()) {
            throw new RuntimeException("La inscripción ya está inactiva");
        }

        inscripcion.setActiva(false);
        inscripcionRepository.save(inscripcion);
        cursoService.actualizarCuposDisponibles(
            inscripcion.getCurso().getIdCurso(), 
            1
        );
    }

    public List<Estudiante> obtenerEstudiantesInscritos(int idCurso) {
        return inscripcionRepository.findByCursoIdCursoAndActivaTrue(idCurso)
                .stream()
                .map(Inscripcion::getEstudiante)
                .toList();
    }

    public List<Curso> obtenerCursosInscritos(String rutEstudiante) {
        validarRut(rutEstudiante);
        String rutFormateado = RutUtils.formatearRut(rutEstudiante);
        
        return inscripcionRepository.findByEstudianteRutAndActivaTrue(rutFormateado)
                .stream()
                .map(Inscripcion::getCurso)
                .toList();
    }

    private void validarInscripcion(int idCurso, String rut, Curso curso) {
        if (!curso.tieneCuposDisponibles()) {
            throw new CupoAgotadoException("No hay cupos disponibles en este curso");
        }

        if (inscripcionRepository.existsByCursoIdCursoAndEstudianteRut(idCurso, rut)) {
            throw new EstudianteYaInscritoException("El estudiante ya está inscrito en este curso");
        }
    }

    private void validarRut(String rut) {
        if (!RutUtils.validarRut(rut)) {
            throw new IllegalArgumentException("RUT inválido. Formato esperado: 12.345.678-9");
        }
    }
}
