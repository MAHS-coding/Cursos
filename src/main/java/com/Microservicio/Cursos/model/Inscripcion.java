package com.Microservicio.Cursos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inscripcion")
@Data
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCurso;

    @Column(nullable = false)
    private Integer idEstudiante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInscripcion estado = EstadoInscripcion.PENDIENTE;
}