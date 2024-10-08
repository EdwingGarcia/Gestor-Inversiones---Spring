package com.edwinggarcia.Inversiones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edwinggarcia.Inversiones.controller.dto.UsuarioRegistroDTO;
import com.edwinggarcia.Inversiones.service.UsuarioService;



@Controller
@RequestMapping("/registro")
public class RegistroUsuarioController {

	private UsuarioService usuarioServicio;

	public RegistroUsuarioController(UsuarioService usuarioServicio) {
		super();
		this.usuarioServicio = usuarioServicio;
	}
	
	@ModelAttribute("usuario")
	public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
		return new UsuarioRegistroDTO();
	}

	@GetMapping
	public String mostrarFormularioDeRegistro() {
		return "registro";
	}
	
	@PostMapping
	public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO) {
		usuarioServicio.guardar(registroDTO);
		return "redirect:/registro?exito";
	}

}
