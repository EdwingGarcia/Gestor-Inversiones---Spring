package com.edwinggarcia.Inversiones.controller;

import com.edwinggarcia.Inversiones.controller.dto.BrokerGananciaDTO;
import com.edwinggarcia.Inversiones.controller.dto.ComparativaDTO;
import com.edwinggarcia.Inversiones.controller.dto.TendenciasDTO;
import com.edwinggarcia.Inversiones.model.Activo;
import com.edwinggarcia.Inversiones.model.Inversion;
import com.edwinggarcia.Inversiones.model.Usuario;
import com.edwinggarcia.Inversiones.repos.UsuarioRepository;
import com.edwinggarcia.Inversiones.service.InversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/inversiones") // Prefijo para todas las rutas
public class InversionController {

	private final InversionService inversionService;
	private final UsuarioRepository usuarioRepository;

	@Autowired
	public InversionController(InversionService inversionService, UsuarioRepository usuarioRepository) {
		this.inversionService = inversionService;
		this.usuarioRepository = usuarioRepository;
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
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		TendenciasDTO tendenciasDTO = inversionService.obtenerTendenciasInversionesGlobales();

		ComparativaDTO comparativaDTO = new ComparativaDTO();
		comparativaDTO.setSumaMontosInvertidosPrimeraLista(0.0);
		comparativaDTO.setGananciasConComisionPrimeraLista(0.0);
		comparativaDTO.setSumaMontosInvertidosSegundaLista(0.0);
		comparativaDTO.setGananciasConComisionSegundaLista(0.0);
		comparativaDTO.setTipoMasRentablePrimeraLista(Collections.emptyList());
		comparativaDTO.setTipoMasRentableSegundaLista(Collections.emptyList());

		return ResponseEntity.ok(comparativaDTO);
	}

	@PostMapping
	public ResponseEntity<Inversion> guardar(@RequestBody Inversion inversion) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		inversion.setEmailUsuario(email);
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
}
