package com.Microservicio.Cursos.exception;

public class CursoNotFoundException extends RuntimeException{
    public CursoNotFoundException(String message)
    {
        super(message);
    }
}