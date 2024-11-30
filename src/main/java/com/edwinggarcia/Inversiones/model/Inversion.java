package com.edwinggarcia.Inversiones.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "inversion")
public class Inversion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String emailUsuario;
	private String nombre;
	private String tipo;
	private double montoInvertido;
	private LocalDate fechaInversion;
	private String estado;
	private String comentarios;
	private double precioInversion;

	@ManyToOne
	@JoinColumn(name = "broker_id", nullable = false)
	private Broker broker;

	@ManyToOne
	@JoinColumn(name = "estrategia_id", nullable = false)
	private Estrategia estrategia;

	@ManyToOne
	@JoinColumn(name = "activo_id", nullable = false)
	private Activo activo;

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getMontoInvertido() {
		return montoInvertido;
	}

	public double getPrecioInversion() {
		return precioInversion;
	}

	public void setPrecioInversion(double precioInversion) {
		this.precioInversion = precioInversion;
	}

	public void setMontoInvertido(double montoInvertido) {
		this.montoInvertido = montoInvertido;
	}

	public LocalDate getFechaInversion() {
		return fechaInversion;
	}

	public void setFechaInversion(LocalDate fechaInversion) {
		this.fechaInversion = fechaInversion;
	}


	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public Estrategia getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(Estrategia estrategia) {
		this.estrategia = estrategia;
	}

	public Activo getActivo() {
		return activo;
	}

	public void setActivo(Activo activo) {
		this.activo = activo;
	}
}
