package com.Microservicio.Cursos.model;

import lombok.Data;

@Data
public class UsuarioDTO {
    private int idUsuario;
    private String tipoUsuario; // "ADMINISTRADOR", "PROFESOR", "ESTUDIANTE"
    private String nombreUsuario; 
    private String apellidoPUsuario; 
    private String apellidoMUsuario; 
    private String emailInstitucional; 
    private boolean activo; 
    private String rol;
}