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
		return inversionRepository.findAll();
	}

	public Inversion guardar(Inversion inversion) {
		return inversionRepository.save(inversion);
	}

	public Inversion obtenerPorId(Long id) {
		return inversionRepository.findById(id).orElse(null);
	}

	public void eliminar(Long id) {
		inversionRepository.deleteById(id);
	}

}
