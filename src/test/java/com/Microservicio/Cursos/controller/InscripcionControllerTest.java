package com.Microservicio.Cursos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.Microservicio.Cursos.model.EstadoInscripcion;
import com.Microservicio.Cursos.model.Inscripcion;
import com.Microservicio.Cursos.service.InscripcionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscripcionController.class)
class InscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscripcionService inscripcionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObtenerTodasLasInscripciones() throws Exception {
        Inscripcion inscripcion1 = new Inscripcion();
        inscripcion1.setIdInscripcion(1L);
        inscripcion1.setIdUsuario(101);
        inscripcion1.setIdCurso(1L);
        inscripcion1.setEstado(EstadoInscripcion.PENDIENTE);
        
        Inscripcion inscripcion2 = new Inscripcion();
        inscripcion2.setIdInscripcion(2L);
        inscripcion2.setIdUsuario(102);
        inscripcion2.setIdCurso(1L);
        inscripcion2.setEstado(EstadoInscripcion.ACEPTADO);

        Mockito.when(inscripcionService.listarInscripciones())
                .thenReturn(Arrays.asList(inscripcion1, inscripcion2));

        mockMvc.perform(get("/api/inscripciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(101))
                .andExpect(jsonPath("$[1].estado").value("ACEPTADO"));
    }

    @Test
    void testObtenerInscripcionesCuandoNoHayRegistros() throws Exception {
        Mockito.when(inscripcionService.listarInscripciones())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/inscripciones"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCrearInscripcion() throws Exception {
        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setIdUsuario(101);
        nuevaInscripcion.setIdCurso(1L);
        nuevaInscripcion.setEstado(EstadoInscripcion.PENDIENTE);
        
        Inscripcion inscripcionGuardada = new Inscripcion();
        inscripcionGuardada.setIdInscripcion(1L);
        inscripcionGuardada.setIdUsuario(101);
        inscripcionGuardada.setIdCurso(1L);
        inscripcionGuardada.setEstado(EstadoInscripcion.PENDIENTE);

        Mockito.when(inscripcionService.crearInscripcion(any(Inscripcion.class)))
                .thenReturn(inscripcionGuardada);

        mockMvc.perform(post("/api/inscripciones/inscribirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaInscripcion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInscripcion").value(1L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testCrearInscripcionConError() throws Exception {
        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setIdUsuario(101);
        nuevaInscripcion.setIdCurso(1L);
        nuevaInscripcion.setEstado(EstadoInscripcion.PENDIENTE);

        Mockito.when(inscripcionService.crearInscripcion(any(Inscripcion.class)))
                .thenThrow(new RuntimeException("Error al crear inscripción"));

        mockMvc.perform(post("/api/inscripciones/inscribirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaInscripcion)))
                .andExpect(status().isConflict());
    }

    @Test
    void testActualizarEstadoAceptado() throws Exception {
        Long idInscripcion = 1L;
        Inscripcion inscripcionActualizada = new Inscripcion();
        inscripcionActualizada.setIdInscripcion(idInscripcion);
        inscripcionActualizada.setIdUsuario(101);
        inscripcionActualizada.setIdCurso(1L);
        inscripcionActualizada.setEstado(EstadoInscripcion.ACEPTADO);
        
        Mockito.when(inscripcionService.cambiarEstado(anyLong(), any(EstadoInscripcion.class)))
                .thenReturn(inscripcionActualizada);

        mockMvc.perform(put("/api/inscripciones/{idInscripcion}/estado", idInscripcion)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"ACEPTADO\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Inscripción aceptada. El usuario ha sido inscrito en el curso."));
    }

    @Test
    void testActualizarEstadoRechazado() throws Exception {
        Long idInscripcion = 1L;
        Inscripcion inscripcionActualizada = new Inscripcion();
        inscripcionActualizada.setIdInscripcion(idInscripcion);
        inscripcionActualizada.setIdUsuario(101);
        inscripcionActualizada.setIdCurso(1L);
        inscripcionActualizada.setEstado(EstadoInscripcion.RECHAZADO);
        
        Mockito.when(inscripcionService.cambiarEstado(anyLong(), any(EstadoInscripcion.class)))
                .thenReturn(inscripcionActualizada);

        mockMvc.perform(put("/api/inscripciones/{idInscripcion}/estado", idInscripcion)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"RECHAZADO\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Inscripción rechazada y eliminada correctamente."));
    }

    @Test
    void testActualizarEstadoPendiente() throws Exception {
        Long idInscripcion = 1L;
        Inscripcion inscripcionActualizada = new Inscripcion();
        inscripcionActualizada.setIdInscripcion(idInscripcion);
        inscripcionActualizada.setIdUsuario(101);
        inscripcionActualizada.setIdCurso(1L);
        inscripcionActualizada.setEstado(EstadoInscripcion.PENDIENTE);
        
        Mockito.when(inscripcionService.cambiarEstado(anyLong(), any(EstadoInscripcion.class)))
                .thenReturn(inscripcionActualizada);

        mockMvc.perform(put("/api/inscripciones/{idInscripcion}/estado", idInscripcion)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"PENDIENTE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }
}