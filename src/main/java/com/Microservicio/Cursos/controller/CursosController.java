package com.Microservicio.Cursos.controller;


@RequestMapping("api/cursos")
@RestController
public class CursosController {
    
    @GetMapping
    public String saludar()
    {
        return "Funciona";
    }
}
