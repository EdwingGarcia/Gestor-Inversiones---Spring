package com.edwinggarcia.Inversiones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edwinggarcia.Inversiones.model.Inversion;
import com.edwinggarcia.Inversiones.service.InversionService;

@Controller
@RequestMapping("/inversiones")
public class InversionController {

	private final InversionService inversionService;

	@Autowired
	public InversionController(InversionService inversionService) {
		this.inversionService = inversionService;
	}


	@GetMapping("/listar")
	public String listar(Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();  // Obtener el email del usuario autenticado
		List<Inversion> inversiones = inversionService.listarInversionesPorUsuario(email);  // Filtrar por email
		//List<Inversion> inversiones = inversionService.listar();  // Filtrar por email
		model.addAttribute("inversiones", inversiones);
		return "index";
	}

	@GetMapping("/agregar")
	public String mostrarFormularioAgregar(Model model) {
		model.addAttribute("inversion", new Inversion());
		return "formulario-agregar";
	}

	@PostMapping("/guardar")
	public String guardar(Inversion inversion) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();  // Obtener el email del usuario autenticado
		inversion.setEmailUsuario(email);  // Asignar el email a la inversión
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

}
