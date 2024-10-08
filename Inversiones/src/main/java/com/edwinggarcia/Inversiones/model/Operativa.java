package com.edwinggarcia.Inversiones.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Entity
public class Operativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha; // Fecha de la operativa
    private String sesion;    // Sesión (e.g., New York, Tokio, London)
    private String divisa;    // Divisa (e.g., EUR/USD, AUD/USD)
    private double stopSize;  // Resultado en Pips
    private String resultado;  // Resultado (e.g., Take Profit, Stop Loss)
    private String observacion4h; // Observación para 4H
    private String observacion1h; // Observación para 1H
    private String observacion15m; // Observación para 15M
    private String observacion5m;  // Observación para 5M
    private String observacion1m;  // Observación para 1M
    private String enlaceImagen4h;
    private String enlaceImagen1h;
    private String enlaceImagen15m;
    private String enlaceImagen5m;
    private String enlaceImagen1m;

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public String getEnlaceImagen15m() {
        return enlaceImagen15m;
    }

    public void setEnlaceImagen15m(String enlaceImagen15m) {
        this.enlaceImagen15m = enlaceImagen15m;
    }

    public String getEnlaceImagen1h() {
        return enlaceImagen1h;
    }

    public void setEnlaceImagen1h(String enlaceImagen1h) {
        this.enlaceImagen1h = enlaceImagen1h;
    }

    public String getEnlaceImagen1m() {
        return enlaceImagen1m;
    }

    public void setEnlaceImagen1m(String enlaceImagen1m) {
        this.enlaceImagen1m = enlaceImagen1m;
    }

    public String getEnlaceImagen4h() {
        return enlaceImagen4h;
    }

    public void setEnlaceImagen4h(String enlaceImagen4h) {
        this.enlaceImagen4h = enlaceImagen4h;
    }

    public String getEnlaceImagen5m() {
        return enlaceImagen5m;
    }

    public void setEnlaceImagen5m(String enlaceImagen5m) {
        this.enlaceImagen5m = enlaceImagen5m;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion15m() {
        return observacion15m;
    }

    public void setObservacion15m(String observacion15m) {
        this.observacion15m = observacion15m;
    }

    public String getObservacion1h() {
        return observacion1h;
    }

    public void setObservacion1h(String observacion1h) {
        this.observacion1h = observacion1h;
    }

    public String getObservacion1m() {
        return observacion1m;
    }

    public void setObservacion1m(String observacion1m) {
        this.observacion1m = observacion1m;
    }

    public String getObservacion4h() {
        return observacion4h;
    }

    public void setObservacion4h(String observacion4h) {
        this.observacion4h = observacion4h;
    }

    public String getObservacion5m() {
        return observacion5m;
    }

    public void setObservacion5m(String observacion5m) {
        this.observacion5m = observacion5m;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public double getStopSize() {
        return stopSize;
    }

    public void setStopSize(double stopSize) {
        this.stopSize = stopSize;
    }
}
