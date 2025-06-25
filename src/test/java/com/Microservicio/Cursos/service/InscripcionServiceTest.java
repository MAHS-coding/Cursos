package com.Microservicio.Cursos.service;

import com.Microservicio.Cursos.model.*;
import com.Microservicio.Cursos.repository.CursoRepository;
import com.Microservicio.Cursos.repository.InscripcionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InscripcionService inscripcionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarInscripciones() {
        // Given
        Inscripcion inscripcion1 = new Inscripcion(1L, 101, 1L, EstadoInscripcion.PENDIENTE);
        Inscripcion inscripcion2 = new Inscripcion(2L, 102, 1L, EstadoInscripcion.PENDIENTE);
        when(inscripcionRepository.findAll()).thenReturn(List.of(inscripcion1, inscripcion2));

        // When
        List<Inscripcion> resultado = inscripcionService.listarInscripciones();

        // Then
        assertThat(resultado)
                .hasSize(2)
                .extracting(Inscripcion::getIdUsuario)
                .containsExactly(101, 102);
        verify(inscripcionRepository).findAll();
    }

    @Test
    void testCrearInscripcion_UsuarioNoEncontrado() {
        // Given
        Inscripcion nuevaInscripcion = new Inscripcion(null, 999, 1L, null);
        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> inscripcionService.crearInscripcion(nuevaInscripcion));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Usuario no encontrado");
        verify(restTemplate).getForObject(anyString(), eq(UsuarioDTO.class));
        verifyNoInteractions(inscripcionRepository);
    }

    @Test
    void testCrearInscripcion_UsuarioNoEstudiante() {
        // Given
        Inscripcion nuevaInscripcion = new Inscripcion(null, 101, 1L, null);
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setTipoUsuario("PROFESOR");
        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(usuario);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> inscripcionService.crearInscripcion(nuevaInscripcion));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(exception.getReason()).isEqualTo("Solo estudiantes pueden inscribirse a un curso");
        verify(restTemplate).getForObject(anyString(), eq(UsuarioDTO.class));
        verifyNoInteractions(inscripcionRepository);
    }

    @Test
    void testCrearInscripcion_InscripcionDuplicada() {
        // Given
        Inscripcion nuevaInscripcion = new Inscripcion(null, 101, 1L, null);
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setTipoUsuario("ESTUDIANTE");
        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(usuario);
        when(inscripcionRepository.findByIdUsuarioAndIdCurso(101, 1L))
                .thenReturn(Optional.of(new Inscripcion()));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> inscripcionService.crearInscripcion(nuevaInscripcion));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getReason()).isEqualTo("Ya existe una inscripción para este curso");
        verify(inscripcionRepository).findByIdUsuarioAndIdCurso(101, 1L);
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testCrearInscripcion_Exitosa() {
        // Given
        Inscripcion nuevaInscripcion = new Inscripcion(null, 101, 1L, null);
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setTipoUsuario("ESTUDIANTE");
        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(usuario);
        when(inscripcionRepository.findByIdUsuarioAndIdCurso(101, 1L)).thenReturn(Optional.empty());
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(invocation -> {
            Inscripcion i = invocation.getArgument(0);
            i.setIdInscripcion(1L);
            return i;
        });

        // When
        Inscripcion resultado = inscripcionService.crearInscripcion(nuevaInscripcion);

        // Then
        assertThat(resultado)
                .extracting(
                        Inscripcion::getIdInscripcion,
                        Inscripcion::getIdUsuario,
                        Inscripcion::getIdCurso,
                        Inscripcion::getEstado)
                .containsExactly(1L, 101, 1L, EstadoInscripcion.PENDIENTE);
        verify(inscripcionRepository).save(any(Inscripcion.class));
    }

    @Test
    void testCambiarEstado_InscripcionNoEncontrada() {
        // Given
        Long idInscripcion = 999L;
        when(inscripcionRepository.findById(idInscripcion)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> inscripcionService.cambiarEstado(idInscripcion, EstadoInscripcion.ACEPTADO));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Inscripción no encontrada");
        verify(inscripcionRepository).findById(idInscripcion);
        verifyNoInteractions(cursoRepository);
    }

    @Test
    void testCambiarEstado_CursoNoEncontrado() {
        // Given
        Long idInscripcion = 1L;
        Inscripcion inscripcion = new Inscripcion(idInscripcion, 101, 999L, EstadoInscripcion.PENDIENTE);
        when(inscripcionRepository.findById(idInscripcion)).thenReturn(Optional.of(inscripcion));
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> inscripcionService.cambiarEstado(idInscripcion, EstadoInscripcion.ACEPTADO));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Curso no encontrado");
        verify(cursoRepository).findById(999L);
    }

    @Test
    void testCambiarEstado_AceptadoSinCupo() {
        // Given
        Long idInscripcion = 1L;
        Long idCurso = 1L;
        Inscripcion inscripcion = new Inscripcion(idInscripcion, 101, idCurso, EstadoInscripcion.PENDIENTE);
        Curso curso = new Curso(idCurso, "Matemáticas", "Álgebra", 10, 0);
        when(inscripcionRepository.findById(idInscripcion)).thenReturn(Optional.of(inscripcion));
        when(cursoRepository.findById(idCurso)).thenReturn(Optional.of(curso));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> inscripcionService.cambiarEstado(idInscripcion, EstadoInscripcion.ACEPTADO));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).isEqualTo("No hay cupos disponibles");
        verify(cursoRepository, never()).save(any());
        verify(inscripcionRepository, never()).delete(any());
    }

    @Test
    void testCambiarEstado_AceptadoConCupo() {
        // Given
        Long idInscripcion = 1L;
        Long idCurso = 1L;
        Inscripcion inscripcion = new Inscripcion(idInscripcion, 101, idCurso, EstadoInscripcion.PENDIENTE);
        Curso curso = new Curso(idCurso, "Matemáticas", "Álgebra", 10, 5);
        when(inscripcionRepository.findById(idInscripcion)).thenReturn(Optional.of(inscripcion));
        when(cursoRepository.findById(idCurso)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // When
        Inscripcion resultado = inscripcionService.cambiarEstado(idInscripcion, EstadoInscripcion.ACEPTADO);

        // Then
        assertThat(resultado).isNull();
        verify(cursoRepository).save(any(Curso.class));
        verify(inscripcionRepository).delete(inscripcion);
        verify(restTemplate).put(anyString(), any(Map.class));
    }

    @Test
    void testCambiarEstado_Rechazado() {
        // Given
        Long idInscripcion = 1L;
        Long idCurso = 101L;

        // Crear inscripción correctamente según el modelo
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdInscripcion(idInscripcion);
        inscripcion.setIdUsuario(1); // int como está definido en el modelo
        inscripcion.setIdCurso(idCurso);
        inscripcion.setEstado(EstadoInscripcion.PENDIENTE);

        when(inscripcionRepository.findById(idInscripcion)).thenReturn(Optional.of(inscripcion));

        // Mockear la verificación del curso
        Curso curso = new Curso();
        when(cursoRepository.findById(idCurso)).thenReturn(Optional.of(curso));

        // When
        Inscripcion resultado = inscripcionService.cambiarEstado(idInscripcion, EstadoInscripcion.RECHAZADO);

        // Then
        assertThat(resultado).isNull();
        verify(inscripcionRepository).delete(inscripcion);
        verify(cursoRepository).findById(idCurso);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testCambiarEstado_OtroEstado() {
        // Given
        Long idInscripcion = 1L;
        Long idCurso = 101L;

        // Crear inscripción inicial
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdInscripcion(idInscripcion);
        inscripcion.setIdUsuario(1); // int como está definido en el modelo
        inscripcion.setIdCurso(idCurso);
        inscripcion.setEstado(EstadoInscripcion.PENDIENTE);

        // Crear inscripción actualizada (puede ser la misma)
        Inscripcion inscripcionActualizada = new Inscripcion();
        inscripcionActualizada.setIdInscripcion(idInscripcion);
        inscripcionActualizada.setIdUsuario(1);
        inscripcionActualizada.setIdCurso(idCurso);
        inscripcionActualizada.setEstado(EstadoInscripcion.PENDIENTE);

        when(inscripcionRepository.findById(idInscripcion)).thenReturn(Optional.of(inscripcion));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcionActualizada);

        // Mockear la verificación del curso
        Curso curso = new Curso();
        when(cursoRepository.findById(idCurso)).thenReturn(Optional.of(curso));

        // When
        Inscripcion resultado = inscripcionService.cambiarEstado(idInscripcion, EstadoInscripcion.PENDIENTE);

        // Then
        assertThat(resultado)
                .extracting(Inscripcion::getEstado)
                .isEqualTo(EstadoInscripcion.PENDIENTE);
        verify(inscripcionRepository).save(any(Inscripcion.class));
        verify(cursoRepository).findById(idCurso);
        verifyNoInteractions(restTemplate);
    }
}