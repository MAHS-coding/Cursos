package com.Microservicio.Cursos.service;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.model.UsuarioDTO;
import com.Microservicio.Cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CursoService {
    @Autowired private CursoRepository cursoRepository;
    @Autowired private RestTemplate restTemplate;

    public Curso crearCurso(Curso curso, Integer idProfesor) {
        UsuarioDTO profesor = obtenerUsuario(idProfesor);
        if (profesor == null || !profesor.isActivo() || !"PROFESOR".equals(profesor.getTipoUsuario())) {
            throw new RuntimeException("El usuario no es un profesor válido o está inactivo");
        }
        
        curso.setIdProfesor(idProfesor);
        curso.setCupoDisponible(curso.getCupoMaximo());
        return cursoRepository.save(curso);
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public List<Curso> listarCursosActivos() {
        return cursoRepository.findByActivoTrue();
    }

    public List<Curso> listarCursosPorProfesor(Integer idProfesor) {
        return cursoRepository.findByIdProfesor(idProfesor);
    }

    public Curso obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    public Curso actualizarCurso(Long id, Curso cursoActualizado) {
        Curso cursoExistente = obtenerCursoPorId(id);
        cursoExistente.setNombre(cursoActualizado.getNombre());
        cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
        cursoExistente.setCupoMaximo(cursoActualizado.getCupoMaximo());
        return cursoRepository.save(cursoExistente);
    }

    public void eliminarCurso(Long id) {
        cursoRepository.deleteById(id);
    }

    private UsuarioDTO obtenerUsuario(Integer idUsuario) {
        String url = "http://localhost:8081/api/usuarios/public/" + idUsuario;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
}