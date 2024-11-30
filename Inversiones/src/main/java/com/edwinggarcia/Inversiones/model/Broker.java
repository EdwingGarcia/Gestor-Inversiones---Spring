package com.edwinggarcia.Inversiones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "broker")
public class Broker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Nombre del broker
    private String pais; // País de operación
    private String sitioWeb; // Sitio web oficial
    private String correoContacto; // Correo de contacto
    private String telefono; // Número de teléfono
    private double depositoMinimo; // Depósito mínimo requerido
    private double comisionPorcentaje; // Comisión por operación
    private boolean soportaForex; // ¿Soporta trading en Forex?
    private boolean soportaAcciones; // ¿Soporta trading en acciones?
    private boolean soportaCriptomonedas; // ¿Soporta trading en criptomonedas?
    private String autoridadReguladora; // Autoridad reguladora (ej. SEC, FCA, etc.)
    private String plataforma; // Plataforma ofrecida (ej. MetaTrader, propia, etc.)
    public Broker() {
    }

    public Broker(String autoridadReguladora, double comisionPorcentaje, String correoContacto, double depositoMinimo, Long id, String nombre, String pais, String plataforma, String sitioWeb, boolean soportaAcciones, boolean soportaCriptomonedas, boolean soportaForex, String telefono) {
        this.autoridadReguladora = autoridadReguladora;
        this.comisionPorcentaje = comisionPorcentaje;
        this.correoContacto = correoContacto;
        this.depositoMinimo = depositoMinimo;
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.plataforma = plataforma;
        this.sitioWeb = sitioWeb;
        this.soportaAcciones = soportaAcciones;
        this.soportaCriptomonedas = soportaCriptomonedas;
        this.soportaForex = soportaForex;
        this.telefono = telefono;
    }

    public String getAutoridadReguladora() {
        return autoridadReguladora;
    }

    public void setAutoridadReguladora(String autoridadReguladora) {
        this.autoridadReguladora = autoridadReguladora;
    }

    public double getComisionPorcentaje() {
        return comisionPorcentaje;
    }

    public void setComisionPorcentaje(double comisionPorcentaje) {
        this.comisionPorcentaje = comisionPorcentaje;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public double getDepositoMinimo() {
        return depositoMinimo;
    }

    public void setDepositoMinimo(double depositoMinimo) {
        this.depositoMinimo = depositoMinimo;
    }

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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public boolean isSoportaAcciones() {
        return soportaAcciones;
    }

    public void setSoportaAcciones(boolean soportaAcciones) {
        this.soportaAcciones = soportaAcciones;
    }

    public boolean isSoportaCriptomonedas() {
        return soportaCriptomonedas;
    }

    public void setSoportaCriptomonedas(boolean soportaCriptomonedas) {
        this.soportaCriptomonedas = soportaCriptomonedas;
    }

    public boolean isSoportaForex() {
        return soportaForex;
    }

    public void setSoportaForex(boolean soportaForex) {
        this.soportaForex = soportaForex;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
