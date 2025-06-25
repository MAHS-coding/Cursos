package com.Microservicio.Cursos.controller;

import com.Microservicio.Cursos.model.Curso;
import com.Microservicio.Cursos.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCursos_ConContenido() throws Exception {
        // Given
        Curso curso1 = new Curso();
        curso1.setIdCurso(1L);
        curso1.setNombreCurso("Matemáticas");
        curso1.setDescripcionCurso("Álgebra lineal");
        curso1.setCupoMaximo(30);
        curso1.setCupoDisponible(10);

        Curso curso2 = new Curso();
        curso2.setIdCurso(2L);
        curso2.setNombreCurso("Programación");
        curso2.setDescripcionCurso("Java Spring Boot");
        curso2.setCupoMaximo(25);
        curso2.setCupoDisponible(15);

        List<Curso> cursos = Arrays.asList(curso1, curso2);
        
        Mockito.when(cursoService.listarCursos()).thenReturn(cursos);

        // When & Then
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCurso").value(1))
                .andExpect(jsonPath("$[0].nombreCurso").value("Matemáticas"))
                .andExpect(jsonPath("$[0].descripcionCurso").value("Álgebra lineal"))
                .andExpect(jsonPath("$[1].idCurso").value(2))
                .andExpect(jsonPath("$[1].nombreCurso").value("Programación"));
    }

    @Test
    void testGetCursos_SinContenido() throws Exception {
        // Given
        Mockito.when(cursoService.listarCursos()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCursoPorId() throws Exception {
        // Given
        Long cursoId = 1L;
        Curso curso = new Curso();
        curso.setIdCurso(cursoId);
        curso.setNombreCurso("Matemáticas");
        curso.setDescripcionCurso("Álgebra lineal");
        curso.setCupoMaximo(30);
        curso.setCupoDisponible(10);
        
        Mockito.when(cursoService.obtenerCursoPorId(cursoId)).thenReturn(curso);

        // When & Then
        mockMvc.perform(get("/api/cursos/{id}", cursoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(cursoId.intValue()))
                .andExpect(jsonPath("$.nombreCurso").value("Matemáticas"))
                .andExpect(jsonPath("$.descripcionCurso").value("Álgebra lineal"));
    }

    @Test
    void testPostCurso_CreadoExitosamente() throws Exception {
        // Given
        Curso nuevoCurso = new Curso();
        nuevoCurso.setNombreCurso("Física");
        nuevoCurso.setDescripcionCurso("Termodinámica");
        nuevoCurso.setCupoMaximo(20);
        nuevoCurso.setCupoDisponible(0);

        Curso cursoGuardado = new Curso();
        cursoGuardado.setIdCurso(1L);
        cursoGuardado.setNombreCurso("Física");
        cursoGuardado.setDescripcionCurso("Termodinámica");
        cursoGuardado.setCupoMaximo(20);
        cursoGuardado.setCupoDisponible(0);
        
        Mockito.when(cursoService.crearCurso(any(Curso.class))).thenReturn(cursoGuardado);

        // When & Then
        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoCurso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Física"))
                .andExpect(jsonPath("$.descripcionCurso").value("Termodinámica"));
    }

    @Test
    void testPostCurso_Conflicto() throws Exception {
        // Given
        Curso nuevoCurso = new Curso();
        nuevoCurso.setNombreCurso("Física");
        nuevoCurso.setDescripcionCurso("Termodinámica");
        nuevoCurso.setCupoMaximo(20);
        nuevoCurso.setCupoDisponible(0);
        
        Mockito.when(cursoService.crearCurso(any(Curso.class)))
                .thenThrow(new RuntimeException("Error de conflicto"));

        // When & Then
        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoCurso)))
                .andExpect(status().isConflict());
    }
}