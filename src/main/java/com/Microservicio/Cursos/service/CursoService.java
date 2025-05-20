package com.Microservicio.Cursos.service;

import org.springframework.stereotype.Service;


@Service
@Transactional
public class CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;
    
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

    public Curso actualizarCurso(int idCurso, Curso cursoActualizado) {
        Curso cursoExistente = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new CursoNotFoundException("Curso con ID " + idCurso + " no encontrado"));

        cursoExistente.setTituloCurso(cursoActualizado.getTituloCurso());
        cursoExistente.setDescripcionCurso(cursoActualizado.getDescripcionCurso());
        cursoExistente.setMaxCupoCurso(cursoActualizado.getMaxCupoCurso());
        cursoExistente.setCuposDisponiblesCurso(cursoActualizado.getCuposDisponiblesCurso());

        return cursoRepository.save(cursoExistente);
    }

    public void actualizarCuposDisponibles(int idCurso, int cantidad) {
        Curso curso = buscarCursoPorId(idCurso);
        curso.actualizarCuposDisponibles(cantidad);
        cursoRepository.save(curso);
    }
}
