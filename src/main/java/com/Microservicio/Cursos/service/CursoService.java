package com.Microservicio.Cursos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.repository.CursoRepository;

import jakarta.transaction.Transactional;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listarCursos()
    {
        return cursoRepository.findAll();
    }

    public Curso guardarCurso(Curso curso)
    {
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso actualizarCurso(int idCurso, Curso cursoActualizado)
    {
        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);

        if (!cursoOptional.isPresent())
        {
            throw new RuntimeException("Curso con ID" + idCurso + "no encontrado,");
        }

        Curso cursoExistente = cursoOptional.get();

        cursoExistente.setTituloCurso(cursoActualizado.getTituloCurso());
        cursoExistente.setDescripcionCurso(cursoActualizado.getDescripcionCurso());
        cursoExistente.setMaxCupoCurso(cursoActualizado.getMaxCupoCurso());
        cursoExistente.setCuposDisponiblesCurso(cursoActualizado.getCuposDisponiblesCurso());

        return cursoRepository.save(cursoExistente);
        
        
    }

}
