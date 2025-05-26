package com.Microservicio.Cursos.model;

import lombok.Data;

@Data
public class UsuarioDTO {
    private int idUsuario;
    private String nombre;
    private String tipo; // "ADMINISTRADOR", "PROFESOR", "ESTUDIANTE"
}