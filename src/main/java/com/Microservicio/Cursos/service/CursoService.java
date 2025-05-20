package com.Microservicio.Cursos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Microservicio.Cursos.exception.CupoAgotadoException;
import com.Microservicio.Cursos.exception.CursoNotFoundException;
import com.Microservicio.Cursos.exception.EstudianteNotFoundException;
import com.Microservicio.Cursos.exception.EstudianteYaInscritoException;
import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.repository.CursoRepository;
import com.Microservicio.Cursos.repository.EstudianteRepository;
import com.Microservicio.Cursos.repository.InscripcionRepository;
import com.Microservicio.Cursos.utils.RutUtils;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private InscripcionRepository inscripcionRepository;

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarCursoPorId(int idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new CursoNotFoundException("Curso con ID " + idCurso + " no encontrado"));
    }

    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso actualizarCurso(int idCurso, Curso cursoActualizado) {

        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);

        if (cursoOptional.isEmpty()) {
            throw new CursoNotFoundException("Curso con ID " + idCurso + " no encontrado");
        }

        Curso cursoExistente = cursoOptional.get();

        cursoExistente.setTituloCurso(cursoActualizado.getTituloCurso());
        cursoExistente.setDescripcionCurso(cursoActualizado.getDescripcionCurso());
        cursoExistente.setMaxCupoCurso(cursoActualizado.getMaxCupoCurso());
        cursoExistente.setCuposDisponiblesCurso(cursoActualizado.getCuposDisponiblesCurso());

        return cursoRepository.save(cursoExistente);
    }

    @Transactional
    public String inscribirEstudiante(int idCurso, String rutEstudiante) {

        if (!RutUtils.validarRut(rutEstudiante)) {
            throw new IllegalArgumentException("RUT inválido. Formato esperado: 12.345.678-9");
        }
        String rutFormateado = RutUtils.formatearRut(rutEstudiante);

        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new CursoNotFoundException("Curso no encontrado"));

        Estudiante estudiante = estudianteRepository.findById(rutFormateado)
                .orElseThrow(() -> new EstudianteNotFoundException("Estudiante no encontrado"));

        if (!curso.tieneCuposDisponibles()) {
            throw new CupoAgotadoException("No hay cupos disponibles en este curso");
        }

        if (inscripcionRepository.existByCursoAndEstudianteRut(idCurso, rutFormateado)) {
            throw new EstudianteYaInscritoException("El estudiante ya está inscrito en este curso");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setCurso(curso);
        inscripcion.setEstudiante(estudiante);

        inscripcionRepository.save(inscripcion);
        curso.reducirCupo();
        cursoRepository.save(curso);

        return String.format("Estudiante %s (%s) inscrito exitosamente al curso %s",
                estudiante.getNombre(), rutFormateado, curso.getTituloCurso());
    }

    public List<Estudiante> obtenerEstudiantesInscritos(int idCurso) {
        return inscripcionRepository.findByCursoIdCurso(idCurso)
                .stream()
                .map(Inscripcion::getEstudiante)
                .toList();
    }

    @Transactional
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        // Validar RUT antes de guardar
        if (!RutUtils.validarRut(estudiante.getRut())) {
            throw new IllegalArgumentException("RUT inválido");
        }

        // Formatear RUT consistentemente
        estudiante.setRut(RutUtils.formatearRut(estudiante.getRut()));

        // Verificar si el estudiante ya existe
        if (estudianteRepository.existsByRut(estudiante.getRut())) {
            throw new RuntimeException("El estudiante con RUT " + estudiante.getRut() + " ya existe");
        }

        return estudianteRepository.save(estudiante);
    }
}
