package com.edwingGarcia.CRUD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edwingGarcia.CRUD.model.Inversion;
import com.edwingGarcia.CRUD.repositorio.InversionRepository;

@Service
public class InversionService {
	 private final InversionRepository inversionRepository;

	    @Autowired
	    public InversionService(InversionRepository inversionRepository) {
	        this.inversionRepository = inversionRepository;
	    }

	    public List<Inversion> listar() {
	        return inversionRepository.findAll(); // Devuelve todas las inversiones
	    }

	    public Inversion guardar(Inversion inversion) {
	        return inversionRepository.save(inversion); // Guarda una nueva inversión
	    }

	    public Inversion obtenerPorId(Long id) {
	        return inversionRepository.findById(id).orElse(null); // Devuelve una inversión por su ID
	    }

	    public void eliminar(Long id) {
	        inversionRepository.deleteById(id); // Elimina una inversión por su ID
	    }

}
