package com.edwinggarcia.Inversiones.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.edwinggarcia.Inversiones.controller.dto.UsuarioRegistroDTO;
import com.edwinggarcia.Inversiones.model.Usuario;



public interface IUsuarioService extends UserDetailsService{

	public Usuario guardar(UsuarioRegistroDTO registroDTO);
	
	public List<Usuario> listarUsuarios();
	
}