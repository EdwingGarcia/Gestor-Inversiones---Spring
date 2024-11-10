package com.edwinggarcia.Inversiones.controller;

import java.util.ArrayList;
import java.util.List;

import com.edwinggarcia.Inversiones.model.Usuario;
import com.edwinggarcia.Inversiones.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.edwinggarcia.Inversiones.model.Inversion;
import com.edwinggarcia.Inversiones.service.InversionService;

@Controller
@RequestMapping("/inversiones")
public class InversionController {

	private final InversionService inversionService;
	private final UsuarioRepository usuarioRepository;


	@Autowired
	public InversionController(InversionService inversionService, UsuarioRepository usuarioRepository
) {
		this.inversionService = inversionService;
		this.usuarioRepository=usuarioRepository;
	}


	@GetMapping("/listar")
	public String listar(Model model) {
		// Obtener el nombre de usuario desde el contexto de seguridad
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		// Buscar el usuario por su email
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario != null) {
			List<Inversion> inversiones = new ArrayList<>();

			if (usuario.getRol().equals("ROLE_AUDITOR")) {
				List<String> listaEmailsAsociados = usuario.getEmailsAsociados();

				for (String i : listaEmailsAsociados) {
					List<Inversion> inversionesPorEmail = inversionService.listarInversionesPorEmail(i);
					inversiones.addAll(inversionesPorEmail);
				}

				model.addAttribute("inversiones", inversiones);
				return "index-auditor";
			} else {

				inversiones = inversionService.listarInversionesPorEmail(email);
				model.addAttribute("inversiones", inversiones);
				return "index";
			}
		} else {
			return "redirect:/login";
		}
	}



	@GetMapping("/agregar")
	public String mostrarFormularioAgregar(Model model) {
		model.addAttribute("inversion", new Inversion());
		return "formulario-agregar";
	}

	@PostMapping("/guardar")
	public String guardar(Inversion inversion, @AuthenticationPrincipal Usuario usuario) {
		if (usuario != null) {
			inversion.setEmailUsuario(usuario.getEmail()); // Asigna el email del usuario autenticado
		}
		inversionService.guardar(inversion);
		return "redirect:/inversiones/listar";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		Inversion inversion = inversionService.obtenerPorId(id);
		model.addAttribute("inversion", inversion);
		return "formulario-editar";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id) {
		inversionService.eliminar(id);
		return "redirect:/inversiones/listar";
	}
	@PostMapping("/agregarCorreo")
	public String agregarCorreo(@RequestParam("correo") String correo) {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(emailUsuario);
		inversionService.agregarCorreoAUsuario(emailUsuario, correo);
		return "redirect:/inversiones/listar";
	}

}
