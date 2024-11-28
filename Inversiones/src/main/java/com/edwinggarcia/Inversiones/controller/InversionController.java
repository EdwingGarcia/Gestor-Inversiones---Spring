package com.edwinggarcia.Inversiones.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/comparativa")
	public String mostrarComparativa() {
		return "comparativa";
	}
	@GetMapping("/filtrar")
	public String filtrarInversiones(
	@RequestParam String fechaInicio,
	@RequestParam String fechaFin,
	Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDate inicio = LocalDate.parse(fechaInicio);
		LocalDate fin = LocalDate.parse(fechaFin);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String inicioFormateado = inicio.format(formatter);
		String finFormateado = fin.format(formatter);
		List<Inversion> inversiones = inversionService.listarInversionesPorRangoFechas(email, inicio, fin);
		model.addAttribute("inversiones", inversiones);
		model.addAttribute("fechaInicio", inicioFormateado);
		model.addAttribute("fechaFin", finFormateado);

		return "comparativa";}

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
