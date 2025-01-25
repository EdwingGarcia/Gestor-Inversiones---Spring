package com.edwinggarcia.Inversiones.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.edwinggarcia.Inversiones.controller.dto.BrokerGananciaDTO;
import com.edwinggarcia.Inversiones.controller.dto.ComparativaDTO;
import com.edwinggarcia.Inversiones.controller.dto.TendenciasDTO;
import com.edwinggarcia.Inversiones.model.*;
import com.edwinggarcia.Inversiones.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InversionService {
	private final InversionRepository inversionRepository;
	private final BrokerRepository brokerRepository;
	private final EstrategiaRepository estrategiaRepository;
	private final ActivoRepository activoRepository;
	private final UsuarioRepository usuarioRepository;

	@Autowired
	public InversionService(InversionRepository inversionRepository, BrokerRepository brokerRepository, EstrategiaRepository estrategiaRepository, ActivoRepository activoRepository, UsuarioRepository usuarioRepository) {
		this.inversionRepository = inversionRepository;
		this.brokerRepository= brokerRepository;
		this.estrategiaRepository=estrategiaRepository;
		this.activoRepository=activoRepository;
		this.usuarioRepository=usuarioRepository;
	}

	public List<Inversion> listar() {
		return inversionRepository.findAll();
	}
	public Inversion guardar(Inversion inversion) {
		return inversionRepository.save(inversion);
	}
	public Inversion obtenerPorId(Long id) {
		return inversionRepository.findById(id).orElse(null);
	}
	public void eliminar(Long id) {
		inversionRepository.deleteById(id);
	}
	public List<Inversion> listarInversionesPorUsuario(String emailUsuario) {
		return inversionRepository.findByEmailUsuario(emailUsuario);
	}
	public List<Inversion> listarInversionesPorRangoFechas(String emailUsuario, LocalDate fechaInicio, LocalDate fechaFin) {
		return inversionRepository.findByEmailUsuarioAndFechaInversionBetween(emailUsuario, fechaInicio, fechaFin);
	}
	public List<Broker> getAllBrokers() {
		return brokerRepository.findAll();
	}
	public List<Estrategia> getAllEstrategias() {
		return estrategiaRepository.findAll();
	}
	public Broker encontrarBrokerporNombre (String nombre){
		return brokerRepository.findByNombre(nombre);
	}
	public List<Activo> getAllActivosDisponibles() {
		return activoRepository.findAll();
	}
	public Activo encontrarActivoPorNombre (String nombre){
		return activoRepository.findBySimbolo(nombre);
	}
	public Estrategia encontrarEstrategiaporNombre (String nombre){
		return estrategiaRepository.findByNombre(nombre);
	}
	public List<Inversion> listarInversionesPorBroker(String emailUsuario,Broker broker) {
		return inversionRepository.findByEmailUsuarioAndBroker(emailUsuario,broker);
	}
	public ComparativaDTO compararTablasInversionesPorFechas(List<Inversion> primeraLista, List<Inversion> segundaLista) {
		if (primeraLista == null || segundaLista == null) {
			return new ComparativaDTO();
		}
		ComparativaDTO comparativa = new ComparativaDTO();

		double[] metricasPrimeraLista = calcularMetricas(primeraLista);
		double[] metricasSegundaLista = calcularMetricas(segundaLista);
		comparativa.setTipoMasRentablePrimeraLista(obtenerTipoYNombreInversionMasRentable(primeraLista));
		comparativa.setTipoMasRentableSegundaLista(obtenerTipoYNombreInversionMasRentable(segundaLista));
		comparativa.setSumaMontosInvertidosPrimeraLista(metricasPrimeraLista[0]);
		comparativa.setGananciasConComisionPrimeraLista(metricasPrimeraLista[1]);
		comparativa.setSumaMontosInvertidosSegundaLista(metricasSegundaLista[0]);
		comparativa.setGananciasConComisionSegundaLista(metricasSegundaLista[1]);

		return comparativa;
	}
	public Map<String, String> obtenerActivosMasRentablesPorCadaTipo(String email) {
		List<Inversion> lista = inversionRepository.findByEmailUsuario(email);
		Map<String, List<Inversion>> inversionesPorTipo = new HashMap<>();
		for (Inversion i : lista) {
			String tipo = i.getTipo();
			inversionesPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(i);
		}
		Map<String, String> activosMasRentablesPorTipo = new HashMap<>();
		for (Map.Entry<String, List<Inversion>> entry : inversionesPorTipo.entrySet()) {
			String tipo = entry.getKey();
			List<Inversion> inversiones = entry.getValue();
			Inversion inversionMasRentable = null;
			double rentabilidadMaxima = Double.MIN_VALUE;

			for (Inversion i : inversiones) {
				double precioActual = i.getActivo().getPrecioActual();
				double precioInversion = i.getPrecioInversion();
				double montoInvertido = i.getMontoInvertido();

				if (precioActual > 0 && precioInversion > 0 && montoInvertido > 0) {
					double rentabilidad = (precioActual - precioInversion) * (montoInvertido / precioInversion);
					if (rentabilidad > rentabilidadMaxima) {
						rentabilidadMaxima = rentabilidad;
						inversionMasRentable = i;
					}
				}
			}

			// Si es primera inversion se asigna
			if (inversionMasRentable == null && inversiones.size() == 1) {
				inversionMasRentable = inversiones.get(0);
			}

			if (inversionMasRentable != null) {
				activosMasRentablesPorTipo.put(tipo, inversionMasRentable.getActivo().getSimbolo());
			} else {
				activosMasRentablesPorTipo.put(tipo, "Sin activos rentables");
			}
		}

		// Devolver el mapa con los activos más rentables por tipo
		return activosMasRentablesPorTipo;
	}
	public Map<String, String> obtenerEstrategiaMasRentablePorCadaTipo(String email) {
		List<Inversion> lista = inversionRepository.findByEmailUsuario(email);

		// Mapa para agrupar inversiones por tipo
		Map<String, List<Inversion>> inversionesPorTipo = new HashMap<>();
		for (Inversion i : lista) {
			String tipo = i.getTipo();
			inversionesPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(i);
		}

		// Mapa para almacenar las mejores estrategias por tipo
		Map<String, String> estrategiasMasRentablesPorTipo = new HashMap<>();

		// Iterar sobre cada tipo de inversión
		for (Map.Entry<String, List<Inversion>> entry : inversionesPorTipo.entrySet()) {
			String tipo = entry.getKey();
			List<Inversion> inversiones = entry.getValue();

			// Variables para almacenar la rentabilidad máxima y las estrategias asociadas
			double rentabilidadMaxima = Double.MIN_VALUE;
			List<String> mejoresEstrategias = new ArrayList<>();

			// Iterar sobre las inversiones de ese tipo
			for (Inversion i : inversiones) {
				String estrategia = i.getEstrategia().getNombre();  // Obtener nombre de la estrategia
				double asertividad = i.getEstrategia().getAsertividad();  // Obtener asertividad (decimal)
				double precioActual = i.getActivo().getPrecioActual();
				double precioInversion = i.getPrecioInversion();

				// Verificar que los valores sean válidos para evitar errores
				if (precioActual > 0 && precioInversion > 0) {
					// Calcular rentabilidad de la inversión
					double rentabilidad = precioActual - precioInversion;

					// Si encontramos una rentabilidad mayor, actualizamos la máxima
					if (rentabilidad > rentabilidadMaxima) {
						rentabilidadMaxima = rentabilidad;
						mejoresEstrategias.clear();  // Limpiar lista de estrategias previas
						mejoresEstrategias.add(estrategia + " (Asertividad: " + String.format("%.2f", asertividad * 100) + "%)");
					}
					// Si la rentabilidad es igual a la máxima, agregamos esta estrategia también
					else if (rentabilidad == rentabilidadMaxima) {
						mejoresEstrategias.add(estrategia + " (Asertividad: " + String.format("%.2f", asertividad * 100) + "%)");
					}
				}
			}

			// Si solo hay una estrategia registrada, asignarla directamente como la mejor estrategia
			if (mejoresEstrategias.isEmpty() && inversiones.size() == 1) {
				String estrategia = inversiones.get(0).getEstrategia().getNombre();
				double asertividad = inversiones.get(0).getEstrategia().getAsertividad();
				mejoresEstrategias.add(estrategia + " (Asertividad: " + String.format("%.2f", asertividad * 100) + "%)");
			}

			// Si se encontraron estrategias rentables, las agregamos al mapa
			if (!mejoresEstrategias.isEmpty()) {
				estrategiasMasRentablesPorTipo.put(tipo, String.join(", ", mejoresEstrategias));
			} else {
				// Si no hay estrategias rentables, agregar tipo con mensaje de no rentable
				estrategiasMasRentablesPorTipo.put(tipo, "Sin estrategias rentables");
			}
		}

		// Devolver el mapa con las mejores estrategias por tipo
		return estrategiasMasRentablesPorTipo;
	}
	public List<Activo> getActivosByTipo(String tipo) {
		return activoRepository.findByTipo(tipo);
	}
	public List<String> listarTiposActivos(){
		return activoRepository.findDistinctTipos();
	}
	public void agregarCorreoAUsuario(String emailUsuario, String correo) {
		Usuario usuario = usuarioRepository.findByEmail(emailUsuario);
		Usuario usuarioAsociado = usuarioRepository.findByEmail(correo);

		if (usuario != null && usuarioAsociado != null) {
			if (!usuario.getEmailsAsociados().contains(correo)) {
				usuario.getEmailsAsociados().add(correo);
				usuarioRepository.save(usuario);
			}
		}
	}
	public List<String> obtenerEmailsAsociados(String emailUsuario){
		Usuario usuario = usuarioRepository.findByEmail(emailUsuario);
		return usuario.getEmailsAsociados();
	}
	public List<Inversion> mostrarListaInversionesEmailAsociados(String emailUsuario, String correoAsociado) {
		Usuario usuario = usuarioRepository.findByEmail(emailUsuario);

		if (usuario != null && usuario.getEmailsAsociados().contains(correoAsociado)) {
			return inversionRepository.findByEmailUsuario(correoAsociado);
		}
		return Collections.emptyList();
	}
	public TendenciasDTO obtenerTendenciasInversionesGlobales() {
		List<Inversion> inversionesTodosUsuarios = inversionRepository.findAll();

		LocalDate fechaLimite = LocalDate.now().minus(3, ChronoUnit.MONTHS);

		// Filtrar las inversiones que fueron realizadas en los últimos 3 meses
		List<Inversion> inversionesUltimosTresMeses = inversionesTodosUsuarios.stream()
				.filter(inversion -> inversion.getFechaInversion().isAfter(fechaLimite))
				.collect(Collectors.toList());

		// Listas para almacenar los datos
		List<String> estrategias = new ArrayList<>();
		List<String> activos = new ArrayList<>();
		List<String> tiposInversion = new ArrayList<>();

		// Extraer datos de las inversiones
		for (Inversion inversion : inversionesUltimosTresMeses) {
			estrategias.add(inversion.getEstrategia().toString());
			activos.add(inversion.getActivo().toString());
			tiposInversion.add(inversion.getActivo().getTipo());
		}

		// Obtener el Top 5 de estrategias más comunes
		List<String> topEstrategias = obtenerTopFrecuencias(estrategias, inversionesUltimosTresMeses.size());

		// Obtener el Top 5 de activos más invertidos
		List<String> topActivos = obtenerTopFrecuencias(activos, inversionesUltimosTresMeses.size());

		// Obtener el tipo de activo más invertido
		String topTipoActivo = obtenerTopFrecuenciaUnica(tiposInversion);

		// Crear TendenciasDTO y establecer los valores usando los métodos set
		TendenciasDTO tendenciasDTO = new TendenciasDTO();
		tendenciasDTO.setTopEstrategias(topEstrategias);
		tendenciasDTO.setTopActivos(topActivos);
		tendenciasDTO.setTopTipoActivo(topTipoActivo);

		return tendenciasDTO;
	}
	public List<BrokerGananciaDTO> obtenerGananciasComisionDeCadaBrokerPorFecha(LocalDate fechaInicioLocal, LocalDate fechaFinLocal) {
		List<Inversion> inversiones = inversionRepository.findByFechaInversionBetween(fechaInicioLocal, fechaFinLocal);
		Map<String, Double> gananciasPorBroker = inversiones.stream()
				.collect(Collectors.groupingBy(
						inversion -> inversion.getBroker().getNombre(),
						Collectors.summingDouble(inversion -> calcularGananciaComision(inversion))
				));

		return gananciasPorBroker.entrySet().stream()
				.map(entry -> {
					BrokerGananciaDTO dto = new BrokerGananciaDTO();
					dto.setBrokerNombre(entry.getKey());
					dto.setGananciasComision(entry.getValue());
					return dto;
				})
				.collect(Collectors.toList());
	}

	private double calcularGananciaComision(Inversion inversion) {
		double precioActual = inversion.getActivo().getPrecioActual();
		double precioInversion = inversion.getPrecioInversion();
		double montoInvertido = inversion.getMontoInvertido();
		return (precioActual  * (montoInvertido / precioInversion) - montoInvertido)*inversion.getBroker().getComisionPorcentaje();
	}
	private double[] calcularMetricas(List<Inversion> lista) {
		double sumaMontosInvertidos = 0.0;
		double gananciasConComision = 0.0;

		for (Inversion inversion : lista) {
			sumaMontosInvertidos += inversion.getMontoInvertido();
			if (inversion.getActivo() != null) {
				double precioActual = inversion.getActivo().getPrecioActual();
				double precioInversion = inversion.getPrecioInversion();
				double comisionBroker = inversion.getBroker().getComisionPorcentaje();

				double cantidadComprada = inversion.getMontoInvertido() / precioInversion;
				double gananciaGenerada = precioActual * cantidadComprada-inversion.getMontoInvertido();
				gananciasConComision += gananciaGenerada - (gananciaGenerada * comisionBroker);
			}
		}

		return new double[]{sumaMontosInvertidos, gananciasConComision};
	}
	private List<String> obtenerTipoYNombreInversionMasRentable(List<Inversion> lista) {
		Map<String, Double> rentabilidadPorTipo = new HashMap<>();
		Map<String, String> nombrePorTipo = new HashMap<>(); // Mapa para almacenar el nombre de la inversión por tipo

		for (Inversion i : lista) {
			double precioActual = i.getActivo().getPrecioActual();
			double precioInversion = i.getPrecioInversion();
			double montoInvertido = i.getMontoInvertido();
			String tipo = i.getTipo();
			double rentabilidad = (precioActual - precioInversion) * (montoInvertido / precioInversion);

			// Acumulamos rentabilidad por tipo
			rentabilidadPorTipo.put(tipo, rentabilidadPorTipo.getOrDefault(tipo, 0.0) + rentabilidad);

			// Guardamos el nombre de la inversión para cada tipo
			if (!nombrePorTipo.containsKey(tipo)) {
				nombrePorTipo.put(tipo, i.getActivo().getSimbolo()); // Se asume que 'getActivo().getNombre()' retorna el nombre del activo
			}
		}


		double rentabilidadMaxima = rentabilidadPorTipo.values().stream()
				.max(Double::compare)
				.orElse(0.0);

		List<String> tiposYNombresMasRentables = rentabilidadPorTipo.entrySet().stream()
				.filter(entry -> entry.getValue() == rentabilidadMaxima)
				.map(entry -> entry.getKey() + ": " + nombrePorTipo.get(entry.getKey())) // Concatenar tipo con nombre
				.collect(Collectors.toList());

		return tiposYNombresMasRentables;
	}
	private List<String> obtenerTopFrecuencias(List<String> items, int total) {
		return items.stream()
				.collect(Collectors.groupingBy(item -> item, Collectors.counting()))
				.entrySet().stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Orden descendente por valor
				.limit(5) // Tomar el Top 5
				.map(entry -> entry.getKey() + " (" + String.format("%.2f", (entry.getValue() * 100.0 / total)) + "%)") // Calcular porcentaje
				.toList();
	}
	private String obtenerTopFrecuenciaUnica(List<String> items) {
		return items.stream()
				.collect(Collectors.groupingBy(item -> item, Collectors.counting()))
				.entrySet().stream()
				.max(Comparator.comparingLong(Map.Entry::getValue))
				.map(entry -> entry.getKey())
				.orElse("No disponible");
	}
}