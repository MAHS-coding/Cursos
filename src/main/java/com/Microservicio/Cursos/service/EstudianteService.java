package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.Cursos.exception.EstudianteNotFoundException;
import com.Microservicio.Cursos.model.Estudiante;
import com.Microservicio.Cursos.repository.EstudianteRepository;
import com.Microservicio.Cursos.utils.RutUtils;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository, RutUtils rutUtils) {
        this.estudianteRepository = estudianteRepository;
    }

    public Estudiante crearEstudiante(Estudiante estudiante) {
        validarEstudiante(estudiante);
        estudiante.setRut(RutUtils.formatearRut(estudiante.getRut()));
        
        if (estudianteRepository.existsById(estudiante.getRut())) {
            throw new RuntimeException("El estudiante con RUT " + estudiante.getRut() + " ya existe");
        }
        
        return estudianteRepository.save(estudiante);
    }

    public Estudiante buscarEstudiantePorRut(String rut) {
        String rutFormateado = RutUtils.formatearRut(rut);
        return estudianteRepository.findById(rutFormateado)
                .orElseThrow(() -> new EstudianteNotFoundException("Estudiante con RUT " + rutFormateado + " no encontrado"));
    }

    public List<Estudiante> listarTodosEstudiantes() {
        return estudianteRepository.findAll();
    }

    private void validarEstudiante(Estudiante estudiante) {
        if (!RutUtils.validarRut(estudiante.getRut())) {
            throw new IllegalArgumentException("RUT inválido");
        }
        if (estudiante.getNombre() == null || estudiante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (estudiante.getEmail() == null || !estudiante.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
}