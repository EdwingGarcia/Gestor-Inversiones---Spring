package com.edwinggarcia.Inversiones.controller;

import com.edwinggarcia.Inversiones.controller.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistroController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/api/login")
	public String iniciarSesion(@RequestBody LoginRequest loginRequest) {
		// Autenticación básica usando Email y Contraseña
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()
				)
		);

		// Si llega hasta aquí, la autenticación fue exitosa
		return "Inicio de sesión exitoso"; // Puede retornar una respuesta de éxito o redirigir a algún recurso protegido
	}
}
