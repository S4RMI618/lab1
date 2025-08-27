package com.example.Lab1.services.Interfaces;

import com.example.Lab1.model.Persona;
import java.util.List;

public interface IPersonaService {
    List<Persona> findAll();
    Persona findById(Long id);
    Persona save(Persona persona);
    void delete(Long id);
}
