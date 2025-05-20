package com.Microservicio.Cursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.Cursos.exception.CursoNotFoundException;
import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.repository.CursoRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class CursoService {

    private final CursoRepository cursoRepository;

    @Autowired
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarTodosCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarCursoPorId(int idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new CursoNotFoundException("Curso con ID " + idCurso + " no encontrado"));
    }

    public Curso crearCurso(Curso curso) {
        if (curso.getMaxCupoCurso() <= 0) {
            throw new IllegalArgumentException("El cupo máximo debe ser mayor a cero");
        }
        curso.setCuposDisponiblesCurso(curso.getMaxCupoCurso());
        return cursoRepository.save(curso);
    }

    public Curso actualizarCurso(int idCurso, Curso cursoActualizado) {
        Curso cursoExistente = buscarCursoPorId(idCurso);

        cursoExistente.setTituloCurso(cursoActualizado.getTituloCurso());
        cursoExistente.setDescripcionCurso(cursoActualizado.getDescripcionCurso());
        
        
        int cuposOcupados = cursoExistente.getMaxCupoCurso() - cursoExistente.getCuposDisponiblesCurso();
        if (cursoActualizado.getMaxCupoCurso() < cuposOcupados) {
            throw new IllegalArgumentException("El nuevo cupo máximo no puede ser menor a los cupos ya ocupados (" + cuposOcupados + ")");
        }
        
        cursoExistente.setMaxCupoCurso(cursoActualizado.getMaxCupoCurso());
        cursoExistente.setCuposDisponiblesCurso(
            cursoActualizado.getMaxCupoCurso() - cuposOcupados
        );

        return cursoRepository.save(cursoExistente);
    }

    public void actualizarCuposDisponibles(int idCurso, int cambio) {
        Curso curso = buscarCursoPorId(idCurso);
        int nuevosCupos = curso.getCuposDisponiblesCurso() + cambio;
        
        if (nuevosCupos < 0 || nuevosCupos > curso.getMaxCupoCurso()) {
            throw new IllegalStateException("Operación inválida sobre los cupos disponibles");
        }
        
        curso.setCuposDisponiblesCurso(nuevosCupos);
        cursoRepository.save(curso);
    }
}
