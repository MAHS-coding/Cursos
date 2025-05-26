package com.Microservicio.Cursos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 500, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer cupoMaximo;

    @Column(nullable = false)
    private Integer cupoDisponible;

    @Column(nullable = false)
    private Long idProfesor;
}