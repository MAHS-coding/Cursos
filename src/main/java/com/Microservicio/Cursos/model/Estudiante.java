package com.Microservicio.Cursos.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "estudiante")
@AllArgsConstructor
@NoArgsConstructor
public class Estudiante {
    
    @Id
    @Column(length = 12, nullable = false, unique = true)
    private String rut;

    @Column(length = 150, nullable = false)
    private String nombre;

    @Column(length = 150, nullable = false)
    private String apellidoP;

    @Column(length = 150, nullable = false)
    private String apellidoM;

    @Column(length = 150, nullable = false, unique = true)
    private String emailInstitucional;

}
