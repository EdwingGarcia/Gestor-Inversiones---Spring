package com.edwinggarcia.Inversiones.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edwinggarcia.Inversiones.controller.dto.UsuarioRegistroDTO;
import com.edwinggarcia.Inversiones.service.UsuarioService;

@RestController
@RequestMapping("/api/registro")
public class RegistroUsuarioController {

	private final UsuarioService usuarioServicio;

	public RegistroUsuarioController(UsuarioService usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}

	@PostMapping
	public ResponseEntity<String> registrarCuentaDeUsuario(@RequestBody UsuarioRegistroDTO registroDTO) {
		try {
			usuarioServicio.guardar(registroDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar usuario: " + e.getMessage());
		}
	}
}
