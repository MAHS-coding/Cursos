package com.Microservicio.Cursos.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCurso;

    @Column(length = 250, nullable = false)
    private String nombreCurso;

    @Column(length = 250, nullable = false)
    private String descripcionCurso;

    @Column(nullable = false)
    private int cuposCursoMax;

    @Column(nullable = false)
    private int cuposCursoDisp;

    @ManyToOne
    @JoinColumn(name = "id_profesor")
    @JsonIncludeProperties({"idProfesor", "nombreProfesor", "emailInstitucional"})
    private Profesor profesor;
    
}
