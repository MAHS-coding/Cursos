package com.Microservicio.Cursos.model;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombreUsuario;
    private String apellidoPUsuario;
    private String apellidoMUsuario;
    private String emailInstitucional;
    private String tipoUsuario;
    private boolean activo;
}