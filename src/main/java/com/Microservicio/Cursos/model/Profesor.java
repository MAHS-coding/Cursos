package com.Microservicio.Cursos.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "profesor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProfesor;

    @Column(length = 250,nullable = false)
    private String nombreProfesor;

    @Column(length = 250,nullable = false)
    private String apellidoProfesor;

    @Column(length = 250,nullable = false)
    private String emailInstitucional;

    @OneToMany(mappedBy = "profesor")
    private List<Curso> cursos = new ArrayList<>();
}
