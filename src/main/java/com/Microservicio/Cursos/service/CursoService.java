package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.model.Profesor;
import com.Microservicio.Cursos.repository.CursoRepository;
import com.Microservicio.Cursos.repository.ProfesorRepository;

import jakarta.transaction.Transactional;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;
    private ProfesorRepository profesorRepository;

    // Listar Cursos existentes
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    // Mostrar curso por id
    public Curso obtenerCursoPorId(int idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso con id " + idCurso + " no encontrado."));
    }

    // Crer un curso nuevo
    public Curso crearCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    // Actualizar la informacion de un curso por id
    public Curso actualizarCurso(Curso curso) {
        if (cursoRepository.existsById(curso.getIdCurso())) {
            return cursoRepository.save(curso);
        } else {
            throw new RuntimeException("Curso con id: " + curso.getIdCurso() + " no encontrado.");
        }
    }

    // Eliminar Curso por idCurso
    public void eliminarCurso(int idCurso) {
        if (!cursoRepository.existsById(idCurso)) {
            throw new RuntimeException("Curso con id " + idCurso + " no encontrado.");
        }
        cursoRepository.deleteById(idCurso);
    }

    // Asociar profesor a curso
    @Transactional
    public Curso asignarProfesorACurso(int idCurso, int idProfesor) {
        // 1. Verificar existencia del curso
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + idCurso));

        // 2. Verificar existencia del profesor
        Profesor profesor = profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + idProfesor));

        // 3. Asignar relación
        curso.setProfesor(profesor);

        // 4. Guardar
        return cursoRepository.save(curso);
    }

    // Desasociar profesor de un curso
    public Curso removerProfesorDeCurso(int idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        curso.setProfesor(null);
        return cursoRepository.save(curso);
    }
}
