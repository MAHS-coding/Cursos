package com.Microservicio.Cursos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInscripcion;

    @Column(nullable = false)
    private Long idCurso;

    @Column(nullable = false)
    private Long idEstudiante;

    @Column(length = 100, nullable = false)
    private String nombreEstudiante;

    @Column(length = 50, nullable = false)
    private String estado; // PENDIENTE, ACEPTADO, RECHAZADO

    @Column(length = 50, nullable = false)
    private String fechaInscripcion;
}