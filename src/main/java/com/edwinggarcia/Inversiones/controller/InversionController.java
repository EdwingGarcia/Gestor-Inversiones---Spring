package com.edwinggarcia.Inversiones.controller;

import com.edwinggarcia.Inversiones.controller.dto.BrokerGananciaDTO;
import com.edwinggarcia.Inversiones.controller.dto.ComparativaDTO;
import com.edwinggarcia.Inversiones.controller.dto.TendenciasDTO;
import com.edwinggarcia.Inversiones.model.*;
import com.edwinggarcia.Inversiones.repos.ActivoRepository;
import com.edwinggarcia.Inversiones.repos.BrokerRepository;
import com.edwinggarcia.Inversiones.repos.EstrategiaRepository;
import com.edwinggarcia.Inversiones.repos.UsuarioRepository;
import com.edwinggarcia.Inversiones.service.AnalisisInversionService;
import com.edwinggarcia.Inversiones.service.InversionService;
import com.edwinggarcia.Inversiones.service.ReferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inversiones") // Prefijo para todas las rutas
public class InversionController {

	private final InversionService inversionService;
	private final UsuarioRepository usuarioRepository;
	private final ActivoRepository activoRepository;
	private final EstrategiaRepository estrategiaRepository;
	private final BrokerRepository brokerRepository;
	private final ReferenciaService referenciaService;


	@Autowired
	public InversionController(InversionService inversionService, UsuarioRepository usuarioRepository, EstrategiaRepository estrategiaRepository, ActivoRepository activoRepository, BrokerRepository brokerRepository,ReferenciaService referenciaService) {
		this.inversionService = inversionService;
		this.usuarioRepository = usuarioRepository;
		this.activoRepository = activoRepository;
		this.estrategiaRepository = estrategiaRepository;
		this.brokerRepository = brokerRepository;
		this.referenciaService=referenciaService;
	}

	@GetMapping("/agregar")
	public ResponseEntity<Map<String, Object>> obtenerDatosFormularioAgregar(@RequestParam(required = false) String tipo) {
		Map<String, Object> response = new HashMap<>();
		response.put("inversion", new Inversion());
		response.put("brokers", referenciaService.getAllBrokers());
		response.put("estrategias", referenciaService.getAllEstrategias());
		response.put("activos", referenciaService.getAllActivosDisponibles());
		response.put("tipos", inversionService.listarTiposActivos());
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<Inversion>> listar(@RequestParam(required = false) String email) {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByEmail(emailUsuario);

		if (usuario == null) {
			return ResponseEntity.status(401).build(); // Unauthorized
		}

		List<Inversion> inversiones;
		if ("ROLE_AUDITOR".equals(usuario.getRol())) {
			inversiones = (email != null && !email.isEmpty())
					? inversionService.listarInversionesPorUsuario(email)
					: usuario.getEmailsAsociados()
					.stream()
					.flatMap(asociado -> inversionService.listarInversionesPorUsuario(asociado).stream())
					.toList();
		} else {
			inversiones = inversionService.listarInversionesPorUsuario(emailUsuario);
		}

		return ResponseEntity.ok(inversiones);
	}

	@GetMapping("/activos/{tipo}")
	public ResponseEntity<List<Activo>> obtenerActivosPorTipo(@PathVariable String tipo) {
		return ResponseEntity.ok(inversionService.getActivosByTipo(tipo));
	}

	@GetMapping("/comparativa")
	public ResponseEntity<ComparativaDTO> mostrarComparativa() {
		ComparativaDTO comparativaDTO = inversionService.compararTablasInversionesPorFechas(
				Collections.emptyList(), Collections.emptyList());
		return ResponseEntity.ok(comparativaDTO);
	}

	@PostMapping("/guardar")
	public ResponseEntity<Inversion> guardar(@RequestBody Inversion inversion) {
		if (inversion.getEmailUsuario() == null || inversion.getEmailUsuario().isEmpty()) {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			inversion.setEmailUsuario(email);
		}
		inversion.setEstado("Activo");
		inversionService.guardar(inversion);
		return ResponseEntity.ok(inversion);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Inversion> mostrarFormularioEditar(@PathVariable Long id) {
		Inversion inversion = inversionService.obtenerPorId(id);
		if (inversion == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(inversion);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		inversionService.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/emails")
	public ResponseEntity<Void> agregarCorreo(@RequestParam("correo") String correo) {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		inversionService.agregarCorreoAUsuario(emailUsuario, correo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/emailsAsociados")
	public ResponseEntity<List<String>> obtenerEmailsAsociados() {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(inversionService.obtenerEmailsAsociados(emailUsuario));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Inversion> actualizarInversion(@PathVariable Long id, @RequestBody Inversion inversionActualizada) {
		Inversion inversionExistente = inversionService.obtenerPorId(id);

		if (inversionExistente == null) {
			return ResponseEntity.notFound().build(); // Retorna 404 si la inversión no existe
		}

		if (inversionActualizada.getActivo() != null && inversionActualizada.getActivo().getId() != null) {
			Activo activoExistente = activoRepository.findById(inversionActualizada.getActivo().getId()).orElse(null);
			if (activoExistente == null) {
				return ResponseEntity.badRequest().body(null);
			}
			inversionExistente.setActivo(activoExistente);
		}

		if (inversionActualizada.getBroker() != null && inversionActualizada.getBroker().getId() != null) {
			Broker brokerExistente = brokerRepository.findById(inversionActualizada.getBroker().getId()).orElse(null);
			if (brokerExistente == null) {
				return ResponseEntity.badRequest().body(null);
			}
			inversionExistente.setBroker(brokerExistente);
		}

		if (inversionActualizada.getEstrategia() != null && inversionActualizada.getEstrategia().getId() != null) {
			Estrategia estrategiaExistente = estrategiaRepository.findById(inversionActualizada.getEstrategia().getId()).orElse(null);
			if (estrategiaExistente == null) {
				return ResponseEntity.badRequest().body(null);
			}
			inversionExistente.setEstrategia(estrategiaExistente);
		}

		inversionExistente.setNombre(inversionActualizada.getNombre());
		inversionExistente.setTipo(inversionActualizada.getTipo());
		inversionExistente.setMontoInvertido(inversionActualizada.getMontoInvertido());
		inversionExistente.setPrecioInversion(inversionActualizada.getPrecioInversion());
		inversionExistente.setFechaInversion(inversionActualizada.getFechaInversion());
		inversionExistente.setComentarios(inversionActualizada.getComentarios());

		inversionService.guardar(inversionExistente);
		return ResponseEntity.ok(inversionExistente);
	}

	@GetMapping("/tendencias")
	public ResponseEntity<TendenciasDTO> obtenerTendencias() {
		TendenciasDTO tendenciasDTO = inversionService.obtenerTendenciasInversionesGlobales();
		return ResponseEntity.ok(tendenciasDTO);
	}

	// Endpoint para obtener las estrategias más rentables por tipo
	@GetMapping("/estrategiasMasRentables")
	public ResponseEntity<Map<String, String>> obtenerEstrategiasMasRentables() {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String, String> estrategiasMasRentables = inversionService.obtenerEstrategiaMasRentablePorCadaTipo(emailUsuario);
		return ResponseEntity.ok(estrategiasMasRentables);
	}

	// Endpoint para obtener los activos más rentables por tipo
	@GetMapping("/activosMasRentables")
	public ResponseEntity<Map<String, String>> obtenerActivosMasRentables() {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String, String> activosMasRentables = inversionService.obtenerActivosMasRentablesPorCadaTipo(emailUsuario);
		return ResponseEntity.ok(activosMasRentables);
	}
}
