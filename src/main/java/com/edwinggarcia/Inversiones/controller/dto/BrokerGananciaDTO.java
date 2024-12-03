package com.edwinggarcia.Inversiones.controller.dto;

import com.edwinggarcia.Inversiones.model.Broker;

public class BrokerGananciaDTO {
    private String brokerNombre;
    private double gananciasComision;


    public String getBrokerNombre() {
        return brokerNombre;
    }

    public void setBrokerNombre(String brokerNombre) {
        this.brokerNombre = brokerNombre;
    }

    public double getGananciasComision() {
        return gananciasComision;
    }

    public void setGananciasComision(double gananciasComision) {
        this.gananciasComision = gananciasComision;
    }
}
