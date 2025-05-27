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

    @Column(name = "nombre_curso", length = 150, nullable = false)
    private String nombreCurso;

    @Column(name = "descripcion_curso", length = 150, nullable = false)
    private String descripcionCurso;

    @Column(name = "cupo_maximo", nullable = false)
    private int cupoMaximo;

    @Column(name = "cupo_disponible", nullable = true)
    private int cupoDisponible;

}