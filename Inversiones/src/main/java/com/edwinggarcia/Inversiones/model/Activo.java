package com.edwinggarcia.Inversiones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "activo")
public class Activo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del activo

    private String simbolo; // Abreviatura o ticker del activo (por ejemplo, BTC, AAPL, etc.)

    private String tipo; // Tipo de activo (por ejemplo, Divisa, Criptomoneda, Índice, etc.)

    private double precioActual; // Precio actual del activo

    // Constructores
    public Activo() {
    }

    public Activo(String simbolo, String tipo, double precioActual) {
        this.simbolo = simbolo;
        this.tipo = tipo;
        this.precioActual = precioActual;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(double precioActual) {
        this.precioActual = precioActual;
    }


}
