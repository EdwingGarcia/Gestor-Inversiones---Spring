package com.edwinggarcia.Inversiones.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.edwinggarcia.Inversiones.controller.dto.ComparativaDTO;
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
	public List<Inversion> listarInversionesPorBroker(String emailUsuario,Broker broker)
	{
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
	public Map<String, String> obtenerActivosMasRentablesPorCadaTipo() {
		List<Inversion> lista = inversionRepository.findAll();

		// Mapa para agrupar inversiones por tipo
		Map<String, List<Inversion>> inversionesPorTipo = new HashMap<>();
		for (Inversion i : lista) {
			String tipo = i.getTipo();
			inversionesPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(i);
		}

		// Mapa para almacenar los activos más rentables por tipo
		Map<String, String> activosMasRentablesPorTipo = new HashMap<>();

		// Iterar sobre cada tipo de inversión
		for (Map.Entry<String, List<Inversion>> entry : inversionesPorTipo.entrySet()) {
			String tipo = entry.getKey();
			List<Inversion> inversiones = entry.getValue();
			Inversion inversionMasRentable = null;
			double rentabilidadMaxima = Double.MIN_VALUE;

			// Iterar sobre las inversiones de ese tipo
			for (Inversion i : inversiones) {
				double precioActual = i.getActivo().getPrecioActual();
				double precioInversion = i.getPrecioInversion();
				double montoInvertido = i.getMontoInvertido();

				// Verificar que los valores sean válidos para evitar errores
				if (precioActual > 0 && precioInversion > 0 && montoInvertido > 0) {
					// Calcular rentabilidad del activo
					double rentabilidad = (precioActual - precioInversion) * (montoInvertido / precioInversion);

					// Si encontramos una rentabilidad mayor, actualizamos la máxima
					if (rentabilidad > rentabilidadMaxima) {
						rentabilidadMaxima = rentabilidad;
						inversionMasRentable = i;
					}
				}
			}

			// Si solo hay una inversión registrada, asignarla como el activo más rentable
			if (inversionMasRentable == null && inversiones.size() == 1) {
				inversionMasRentable = inversiones.get(0);
			}

			// Si se encontró una inversión rentable, agregamos el símbolo del activo
			if (inversionMasRentable != null) {
				activosMasRentablesPorTipo.put(tipo, inversionMasRentable.getActivo().getSimbolo());
			} else {
				// Si no hay inversiones rentables, agregar tipo con mensaje de no rentable
				activosMasRentablesPorTipo.put(tipo, "Sin activos rentables");
			}
		}

		// Devolver el mapa con los activos más rentables por tipo
		return activosMasRentablesPorTipo;
	}


	public Map<String, String> obtenerEstrategiaMasRentablePorCadaTipo() {
		List<Inversion> lista = inversionRepository.findAll();

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

		if (usuario != null && usuarioAsociado != null) { // Ambos usuarios deben existir
			if (!usuario.getEmailsAsociados().contains(correo)) { // El correo no debe estar ya asociado
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
		return Collections.emptyList(); // Si no se encuentra o no está asociado, retornar una lista vacía
	}


}