package com.Microservicio.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.Cursos.model.Profesor;
import com.Microservicio.Cursos.service.ProfesorService;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {
    
    @Autowired
    private ProfesorService profesorService;

    //Mostrar a todos los profesores
    @GetMapping
    public ResponseEntity<List<Profesor>> listarProfesores()
    {
        return new ResponseEntity<>(profesorService.listarProfesores(),HttpStatus.OK);
    }

    //Mostrar profesores por id
    @GetMapping("/{idProfesor}")
    public ResponseEntity<Profesor> ontenerProfesorPorId(@PathVariable int idProfesor)
    {
        return new ResponseEntity<>(profesorService.ontenerProfesorPorId(idProfesor),HttpStatus.OK);
    }

    //Crear un nuevo profesor
    @PostMapping
    public ResponseEntity<Profesor> creaProfesor(@RequestBody Profesor profesor)
    {
        return new ResponseEntity<>(profesorService.creaProfesor(profesor), HttpStatus.CREATED);
    }

    @PutMapping("/{idProfesor}")
    public ResponseEntity<Profesor> actualizarProfesor(
            @PathVariable int idProfesor,
            @RequestBody Profesor profesorActualizado) {
        
        Profesor profesor = profesorService.actualizarProfesor(idProfesor, profesorActualizado);
        return ResponseEntity.ok(profesor);
    }

    @DeleteMapping("/{idProfesor}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable int idProfesor)
    {
        profesorService.eliminarProfesor(idProfesor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
