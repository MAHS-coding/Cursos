package com.Microservicio.Cursos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
@Data
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInscripcion;
    
    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;
    
    @ManyToOne
    @JoinColumn(name = "rut_estudiante", nullable = false)
    private Estudiante estudiante;
    
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion = LocalDateTime.now();
    
    @Column(name = "activa", nullable = false)
    private boolean activa = true;
}