package com.edwinggarcia.Inversiones.controller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComparativaDTO {
    private double sumaMontosInvertidosPrimeraLista=0.0;
    private double gananciasConComisionPrimeraLista=0.0;
    private double sumaMontosInvertidosSegundaLista=0.0;
    private double gananciasConComisionSegundaLista=0.0;
    private List<String> tipoMasRentablePrimeraLista;
    private List<String> tipoMasRentableSegundaLista;


    public List<String> getTipoMasRentablePrimeraLista() {
        return tipoMasRentablePrimeraLista;
    }

    public void setTipoMasRentablePrimeraLista(List<String> tipoMasRentablePrimeraLista) {
        this.tipoMasRentablePrimeraLista = tipoMasRentablePrimeraLista;
    }

    public List<String> getTipoMasRentableSegundaLista() {
        return tipoMasRentableSegundaLista;
    }

    public void setTipoMasRentableSegundaLista(List<String> tipoMasRentableSegundaLista) {
        this.tipoMasRentableSegundaLista = tipoMasRentableSegundaLista;
    }

    public double getGananciasConComisionPrimeraLista() {
        return gananciasConComisionPrimeraLista;
    }

    public void setGananciasConComisionPrimeraLista(double gananciasConComisionPrimeraLista) {
        this.gananciasConComisionPrimeraLista = gananciasConComisionPrimeraLista;
    }

    public double getGananciasConComisionSegundaLista() {
        return gananciasConComisionSegundaLista;
    }

    public void setGananciasConComisionSegundaLista(double gananciasConComisionSegundaLista) {
        this.gananciasConComisionSegundaLista = gananciasConComisionSegundaLista;
    }

    public double getSumaMontosInvertidosPrimeraLista() {
        return sumaMontosInvertidosPrimeraLista;
    }

    public void setSumaMontosInvertidosPrimeraLista(double sumaMontosInvertidosPrimeraLista) {
        this.sumaMontosInvertidosPrimeraLista = sumaMontosInvertidosPrimeraLista;
    }

    public double getSumaMontosInvertidosSegundaLista() {
        return sumaMontosInvertidosSegundaLista;
    }

    public void setSumaMontosInvertidosSegundaLista(double sumaMontosInvertidosSegundaLista) {
        this.sumaMontosInvertidosSegundaLista = sumaMontosInvertidosSegundaLista;
    }
}
