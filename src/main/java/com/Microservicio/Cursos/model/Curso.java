package com.Microservicio.Cursos.model;

import lombok.Data;



@Data
public class Curso {
    
    private int idCurso;
    private String tituloCurso;
    private String descripcionCurso;
    private int maxCupoCurso;
    private int cuposDisponiblesCurso;
}
