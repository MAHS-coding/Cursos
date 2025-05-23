package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.repository.EstudianteRepository;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteRepository estudianteRepository;

    // Obtener a todos los profesores
    public List<Estudiante> listarEstudiantes()
    {
        return estudianteRepository.findAll();
    }

    // Obtener estudiante por rut
    public Estudiante obtenerEstudiantePorRut(String rut)
    {
        return estudianteRepository.findById(rut)
                .orElseThrow(() -> new RuntimeException("Estudiante con rut " + rut + " no encontrado"));
    }

    // Crear un estudiante nuevo
    public Estudiante crearEstudiante(Estudiante estudiante)
    {
        return estudianteRepository.save(estudiante);
    }

    // Actualizar la informacion de un profesor por rut
    public Estudiante actualizarEstudiante(String rut, Estudiante estudianteActualizado)
    {
        Estudiante estudianteExistente = estudianteRepository.findById(rut)
                    .orElseThrow(() -> new RuntimeException("Estudiante con rut" + rut + "no encontrado"));

        estudianteExistente.setNombre(estudianteActualizado.getNombre());
        estudianteExistente.setApellidoP(estudianteActualizado.getApellidoP());
        estudianteExistente.setApellidoM(estudianteActualizado.getApellidoM());
        estudianteExistente.setEmailInstitucional(estudianteActualizado.getEmailInstitucional());

        return estudianteRepository.save(estudianteExistente);
    }

    // Eliminar a un estudiante por rut
    public void eliminarEstudiante(String rut)
    {
        Estudiante estudiante = obtenerEstudiantePorRut(rut);
        estudianteRepository.delete(estudiante);
    }
}



