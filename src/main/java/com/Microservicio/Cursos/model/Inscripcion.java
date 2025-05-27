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

    @Column(name = "idEstudiante", nullable = false)
    private int idUsuario;

    @Column(name = "idCurso", nullable = false)
    private Long idCurso;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado = EstadoInscripcion.PENDIENTE;

    @Transient
    private String emailUsuario;

    @Transient
    private String nombreCurso;

}
