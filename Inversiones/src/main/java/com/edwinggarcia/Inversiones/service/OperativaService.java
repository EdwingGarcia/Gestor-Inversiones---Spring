package com.edwinggarcia.Inversiones.service;

import com.edwinggarcia.Inversiones.model.Operativa;
import com.edwinggarcia.Inversiones.repos.OperativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OperativaService {
    @Autowired
    private OperativaRepository registroOperativaRepository;

    public Operativa guardar(Operativa registroOperativa) {
        return registroOperativaRepository.save(registroOperativa);
    }

    public List<Operativa> obtenerTodas() {
        return registroOperativaRepository.findAll();
    }

    // Obtener una operativa por ID
    public Optional<Operativa> obtenerPorId(Long id) {
        return registroOperativaRepository.findById(id);
    }

    public void eliminar(Long id) {
        registroOperativaRepository.deleteById(id);
    }
}
