package com.Microservicio.Cursos.service;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarCursos() {
        // Given
        Curso curso1 = new Curso(1L, "Matemáticas", "Curso de álgebra lineal", 30, 30);
        Curso curso2 = new Curso(2L, "Historia", "Historia universal", 25, 25);
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso1, curso2));

        // When
        List<Curso> resultado = cursoService.listarCursos();

        // Then
        assertThat(resultado)
            .hasSize(2)
            .extracting(Curso::getNombreCurso)
            .containsExactly("Matemáticas", "Historia");
        verify(cursoRepository).findAll();
    }

    @Test
    void testObtenerCursoPorId_Existente() {
        // Given
        Long id = 1L;
        Curso curso = new Curso(id, "Física", "Física cuántica", 20, 20);
        when(cursoRepository.findById(id)).thenReturn(Optional.of(curso));

        // When
        Curso resultado = cursoService.obtenerCursoPorId(id);

        // Then
        assertThat(resultado)
            .isNotNull()
            .extracting(Curso::getNombreCurso, Curso::getDescripcionCurso)
            .containsExactly("Física", "Física cuántica");
        verify(cursoRepository).findById(id);
    }

    @Test
    void testObtenerCursoPorId_NoExistente() {
        // Given
        Long id = 99L;
        when(cursoRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> cursoService.obtenerCursoPorId(id));
        
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Curso no encontrado");
        verify(cursoRepository).findById(id);
    }

    @Test
    void testCrearCurso_Valido() {
        // Given
        Curso cursoNuevo = new Curso(null, "Química", "Química orgánica", 15, 0);
        Curso cursoGuardado = new Curso(1L, "Química", "Química orgánica", 15, 15);
        when(cursoRepository.save(cursoNuevo)).thenReturn(cursoGuardado);

        // When
        Curso resultado = cursoService.crearCurso(cursoNuevo);

        // Then
        assertThat(resultado)
            .extracting(
                Curso::getIdCurso,
                Curso::getNombreCurso,
                Curso::getCupoMaximo,
                Curso::getCupoDisponible
            )
            .containsExactly(1L, "Química", 15, 15);
        verify(cursoRepository).save(cursoNuevo);
    }

    @Test
    void testCrearCurso_CupoInvalido() {
        // Given
        Curso cursoInvalido = new Curso(null, "Biología", "Biología molecular", 0, 0);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cursoService.crearCurso(cursoInvalido));
        
        assertThat(exception.getMessage()).isEqualTo("El cupo maximo debe ser mayor a 0");
        verifyNoInteractions(cursoRepository);
    }
}