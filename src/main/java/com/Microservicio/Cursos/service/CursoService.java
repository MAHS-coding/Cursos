package com.Microservicio.Cursos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Microservicio.Cursos.exception.CursoNotFoundException;
import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.repository.CursoRepository;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarCursoPorId(int idCurso)
    {
        return cursoRepository.findById(idCurso)
        .orElseThrow(()-> new CursoNotFoundException("Curso con ID " + idCurso + " no encontrado"));
    }

    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso actualizarCurso(int idCurso, Curso cursoActualizado) {
        // Buscar el curso existente
        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);
        
        if (cursoOptional.isEmpty()) {
            throw new CursoNotFoundException("Curso con ID " + idCurso + " no encontrado");
        }

        // Obtener el curso existente
        Curso cursoExistente = cursoOptional.get();

        // Actualizar campos (excepto el ID)
        cursoExistente.setTituloCurso(cursoActualizado.getTituloCurso());
        cursoExistente.setDescripcionCurso(cursoActualizado.getDescripcionCurso());
        cursoExistente.setMaxCupoCurso(cursoActualizado.getMaxCupoCurso());
        cursoExistente.setCuposDisponiblesCurso(cursoActualizado.getCuposDisponiblesCurso());

        // Guardar los cambios
        return cursoRepository.save(cursoExistente);
    }
}