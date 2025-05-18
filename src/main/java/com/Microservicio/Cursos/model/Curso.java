package com.Microservicio.Cursos.model;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCurso;

    @Column(name = "nombre_de_curso", length = 250, nullable = false)
    private String tituloCurso;

    @Column(name = "descripcion", length = 250, nullable = false)
    private String descripcionCurso;

    @Column(name = "cupos_maximos", nullable = false)
    private int maxCupoCurso;

    @Column(name = "cupos_disponibles", nullable = false)
    private int cuposDisponiblesCurso;

}
