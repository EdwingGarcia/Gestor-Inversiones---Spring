package com.edwinggarcia.Inversiones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edwinggarcia.Inversiones.model.Inversion;
import com.edwinggarcia.Inversiones.repos.InversionRepository;

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

	public List<Inversion> listarInversionesPorUsuario(String emailUsuario) {
		return inversionRepository.findByEmailUsuario(emailUsuario);
	}

}