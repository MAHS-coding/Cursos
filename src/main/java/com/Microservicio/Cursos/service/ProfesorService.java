package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.Cursos.model.Profesor;
import com.Microservicio.Cursos.repository.ProfesorRepository;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    // Obtener a todos los profesores
    public List<Profesor> listarProfesores() {
        return profesorRepository.findAll();
    }

    // Obtener profesor por idProfesor
    public Profesor ontenerProfesorPorId(int idProfesor) {
        return profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new RuntimeException("Profesor con id " + idProfesor + " no encontrado"));
    }

    // Crear un profesor nuevo
    public Profesor creaProfesor(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    // Actualizar la informacion de un profesor por idProfesor
    public Profesor actualizarProfesor(int idProfesor, Profesor profesorActualizado) {
        Profesor profesorExistente = profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + idProfesor));

        // Actualizar solo los campos permitidos
        profesorExistente.setNombreProfesor(profesorActualizado.getNombreProfesor());
        profesorExistente.setApellidoProfesor(profesorActualizado.getApellidoProfesor());
        profesorExistente.setEmailInstitucional(profesorActualizado.getEmailInstitucional());

        return profesorRepository.save(profesorExistente);
    }

    // Eliminar a un profesor por idProfesor
    public void eliminarProfesor(int idProfesor) {
        Profesor profesor = ontenerProfesorPorId(idProfesor);
        profesorRepository.delete(profesor);
    }
}
