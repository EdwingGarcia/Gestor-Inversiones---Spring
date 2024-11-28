package com.edwinggarcia.Inversiones.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inversion")
public class Inversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String emailUsuario;
    private String nombre;
    private String tipo; 
    private BigDecimal montoInvertido; 
    private LocalDate fechaInversion; 
    private BigDecimal valorActual;
    private BigDecimal rendimiento;
    private LocalDate fechaVencimiento;
    private String estado;
    private String comentarios; 
  
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getMontoInvertido() {
		return montoInvertido;
	}

	public void setMontoInvertido(BigDecimal montoInvertido) {
		this.montoInvertido = montoInvertido;
	}

	public LocalDate getFechaInversion() {
		return fechaInversion;
	}

	public void setFechaInversion(LocalDate fechaInversion) {
		this.fechaInversion = fechaInversion;
	}

	public BigDecimal getValorActual() {
		return valorActual;
	}

	public void setValorActual(BigDecimal valorActual) {
		this.valorActual = valorActual;
	}

	public BigDecimal getRendimiento() {
		return rendimiento;
	}

	public void setRendimiento(BigDecimal rendimiento) {
		this.rendimiento = rendimiento;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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
	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
}
