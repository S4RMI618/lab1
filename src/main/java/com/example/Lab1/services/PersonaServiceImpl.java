package com.example.Lab1.services;
import com.example.Lab1.model.Persona;
import java.util.List;
import com.example.Lab1.repository.PersonaRepository;
import com.example.Lab1.services.Interfaces.IPersonaService;
import org.springframework.stereotype.Service;

@Service
public class PersonaServiceImpl implements IPersonaService {
    private final PersonaRepository personaRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    @Override
    public Persona findById(Long id) {
        return personaRepository.findById(id).orElse(null);
    }

    @Override
    public Persona save(Persona persona) {
        return personaRepository.save(persona);
    }

    @Override
    public void delete(Long id) {
        personaRepository.deleteById(id);
    }
}
