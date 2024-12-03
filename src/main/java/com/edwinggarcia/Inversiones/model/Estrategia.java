package com.edwinggarcia.Inversiones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estrategia")
public class Estrategia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private double asertividad;

    public Estrategia() {
    }

    public Estrategia(double asertividad, String descripcion, Long id, String nombre) {
        this.asertividad = asertividad;
        this.descripcion = descripcion;
        this.id = id;
        this.nombre = nombre;
    }

    public double getAsertividad() {
        return asertividad;
    }

    public void setAsertividad(double asertividad) {
        this.asertividad = asertividad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    @Override
    public String toString() {
        return nombre;  // Devuelve el símbolo del activo
    }
}
