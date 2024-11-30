package com.edwinggarcia.Inversiones.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.edwinggarcia.Inversiones.controller.dto.ComparativaDTO;
import com.edwinggarcia.Inversiones.model.Activo;
import com.edwinggarcia.Inversiones.model.Usuario;
import com.edwinggarcia.Inversiones.repos.UsuarioRepository;
import com.edwinggarcia.Inversiones.service.UsuarioService;
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
	private final UsuarioRepository usuarioRepository;

	@Autowired
	public InversionController(InversionService inversionService,UsuarioRepository usuarioRepository) {
		this.inversionService = inversionService;
		this.usuarioRepository=usuarioRepository;
	}


	@GetMapping("/listar")
	public String listar(@RequestParam(required = false) String email, Model model) {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

		Usuario usuario = usuarioRepository.findByEmail(emailUsuario);
		if (usuario != null) {
			List<Inversion> inversiones = new ArrayList<>();

			if (usuario.getRol().equals("ROLE_AUDITOR")) {
				if (email != null && !email.isEmpty()) {
					// Filtrar por el correo asociado seleccionado
					inversiones = inversionService.listarInversionesPorUsuario(email);
				} else {
					// Mostrar todas las inversiones de los correos asociados
					List<String> listaEmailsAsociados = usuario.getEmailsAsociados();
					for (String asociado : listaEmailsAsociados) {
						inversiones.addAll(inversionService.listarInversionesPorUsuario(asociado));
					}
				}
				model.addAttribute("inversiones", inversiones);
				return "index-auditor";
			} else {
				// Usuario normal: listar inversiones propias
				inversiones = inversionService.listarInversionesPorUsuario(emailUsuario);
				model.addAttribute("inversiones", inversiones);
				return "index";
			}
		} else {
			return "redirect:/login";
		}
	}



	@GetMapping("/agregar")
	public String mostrarFormularioAgregar(@RequestParam(required = false) String tipo,Model model) {
		model.addAttribute("inversion", new Inversion());
		model.addAttribute("brokers", inversionService.getAllBrokers());
		model.addAttribute("estrategias", inversionService.getAllEstrategias());
		model.addAttribute("activos", inversionService.getAllActivosDisponibles());
		model.addAttribute("tipos", inversionService.listarTiposActivos());
		return "formulario-agregar";
	}
	@GetMapping("/activos/{tipo}")
	@ResponseBody
	public List<Activo> obtenerActivosPorTipo(@PathVariable String tipo) {
		return inversionService.getActivosByTipo(tipo);
	}

	@GetMapping("/comparativa")
	public String mostrarComparativa(Model model) {
		ComparativaDTO comparativaDTO = new ComparativaDTO();
		comparativaDTO.setSumaMontosInvertidosPrimeraLista(0.0);
		comparativaDTO.setGananciasConComisionPrimeraLista(0.0);
		comparativaDTO.setSumaMontosInvertidosSegundaLista(0.0);
		comparativaDTO.setGananciasConComisionSegundaLista(0.0);
		comparativaDTO.setTipoMasRentablePrimeraLista(Collections.emptyList());
		comparativaDTO.setTipoMasRentableSegundaLista(Collections.emptyList());
		model.addAttribute("estrategiasMasRentablesPorTipo", inversionService.obtenerEstrategiaMasRentablePorCadaTipo());
		model.addAttribute("activosMasRentablesPorCadaTipoListaGeneral", inversionService.obtenerActivosMasRentablesPorCadaTipo());
		model.addAttribute("comparativadto", comparativaDTO);

		return "comparativa"; // Nombre de la vista HTML
	}


	@GetMapping("/comparar")
	public String compararInversiones(
			@RequestParam(required = false) String fechaInicio1,
			@RequestParam(required = false) String fechaFin1,
			@RequestParam(required = false) String fechaInicio2,
			@RequestParam(required = false) String fechaFin2,
			Model model) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDate inicio1 = (fechaInicio1 != null && !fechaInicio1.isEmpty()) ? LocalDate.parse(fechaInicio1) : null;
		LocalDate fin1 = (fechaFin1 != null && !fechaFin1.isEmpty()) ? LocalDate.parse(fechaFin1) : null;
		LocalDate inicio2 = (fechaInicio2 != null && !fechaInicio2.isEmpty()) ? LocalDate.parse(fechaInicio2) : null;
		LocalDate fin2 = (fechaFin2 != null && !fechaFin2.isEmpty()) ? LocalDate.parse(fechaFin2) : null;
		List<Inversion> inversionesTabla1 = inversionService.listarInversionesPorRangoFechas(email, inicio1, fin1);
		List<Inversion> inversionesTabla2 = inversionService.listarInversionesPorRangoFechas(email, inicio2, fin2);
		ComparativaDTO comparativadto = inversionService.compararTablasInversionesPorFechas(
				inversionesTabla1 != null ? inversionesTabla1 : Collections.emptyList(),
				inversionesTabla2 != null ? inversionesTabla2 : Collections.emptyList()
		);
		model.addAttribute("comparativadto", comparativadto);
		model.addAttribute("inversionesTabla1", inversionesTabla1);
		model.addAttribute("inversionesTabla2", inversionesTabla2);
		model.addAttribute("fechaInicio1", fechaInicio1);
		model.addAttribute("fechaFin1", fechaFin1);
		model.addAttribute("fechaInicio2", fechaInicio2);
		model.addAttribute("fechaFin2", fechaFin2);
		model.addAttribute("estrategiasMasRentablesPorTipo", inversionService.obtenerEstrategiaMasRentablePorCadaTipo());
		model.addAttribute("activosMasRentablesPorCadaTipoListaGeneral", inversionService.obtenerActivosMasRentablesPorCadaTipo());
		return "comparativa";
	}



	@PostMapping("/guardar")
	public String guardar(Inversion inversion, @RequestParam("brokerNombre") String brokerNombre,  @RequestParam("estrategiaNombre") String estrategiaNombre, @RequestParam("activoSimbolo") String activoSimbolo) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		inversion.setBroker(inversionService.encontrarBrokerporNombre(brokerNombre));
		inversion.setEstrategia(inversionService.encontrarEstrategiaporNombre(estrategiaNombre));
		inversion.setActivo(inversionService.encontrarActivoPorNombre(activoSimbolo));
		inversion.setEmailUsuario(email);  // Asignar el email a la inversión
		inversion.setEstado("Activo");
		inversionService.guardar(inversion);
		return "redirect:/inversiones/listar";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
		Inversion inversion = inversionService.obtenerPorId(id);
		model.addAttribute("inversion", inversion);
		model.addAttribute("brokers", inversionService.getAllBrokers());
		model.addAttribute("estrategias", inversionService.getAllEstrategias());
		model.addAttribute("activos", inversionService.getAllActivosDisponibles());
		model.addAttribute("tipos", inversionService.listarTiposActivos());
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

	@GetMapping("/emailsAsociados")
	@ResponseBody
	public List<String> obtenerEmailsAsociados() {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		return inversionService.obtenerEmailsAsociados(emailUsuario);
	}

}
