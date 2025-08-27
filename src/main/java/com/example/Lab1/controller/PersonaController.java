package com.example.Lab1.controller;

import com.example.Lab1.model.Persona;
import com.example.Lab1.services.Interfaces.IPersonaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personas")
public class PersonaController {
    private final IPersonaService personaService;

    public PersonaController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<Persona> listar() {
        return personaService.findAll();
    }

    @GetMapping("/{id}")
    public Persona buscar(@PathVariable Long id) {
        return personaService.findById(id);
    }

    @PostMapping
    public Persona crear(@RequestBody Persona persona) {
        return personaService.save(persona);
    }

    @PutMapping("/{id}")
    public Persona actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        Persona existente = personaService.findById(id);
        if (existente != null) {
            existente.setPnombre(persona.getPnombre());
            existente.setEdad(persona.getEdad());
            return personaService.save(existente);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        personaService.delete(id);
    }
}
