package com.edwinggarcia.Inversiones.service;

import java.util.List;

import com.edwinggarcia.Inversiones.model.Usuario;
import com.edwinggarcia.Inversiones.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edwinggarcia.Inversiones.model.Inversion;
import com.edwinggarcia.Inversiones.repos.InversionRepository;

@Service
public class InversionService {
	private final InversionRepository inversionRepository;
	private final UsuarioRepository usuarioRepository;
	@Autowired
	public InversionService(InversionRepository inversionRepository,UsuarioRepository usuarioRepository) {
		this.inversionRepository = inversionRepository;
		this.usuarioRepository=usuarioRepository;
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

	public List<Inversion> listarInversionesPorEmail(String emailUsuario) {
		return inversionRepository.findByEmailUsuario(emailUsuario);
	}
	public void agregarCorreoAUsuario(String emailUsuario, String correo) {
		Usuario usuario = usuarioRepository.findByEmail(emailUsuario);

		if (usuario != null) {
			if (!usuario.getEmailsAsociados().contains(correo)) {
				usuario.getEmailsAsociados().add(correo);
				usuarioRepository.save(usuario);
			}
		}
	}

}