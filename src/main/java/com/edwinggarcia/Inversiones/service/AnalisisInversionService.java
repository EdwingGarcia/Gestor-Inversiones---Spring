package com.edwinggarcia.Inversiones.service;

import com.edwinggarcia.Inversiones.model.Inversion;
import com.edwinggarcia.Inversiones.repos.InversionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalisisInversionService {
    private final InversionRepository inversionRepository;

    @Autowired
    public AnalisisInversionService(InversionRepository inversionRepository) {
        this.inversionRepository = inversionRepository;
    }

    public double[] calcularMetricas(List<Inversion> lista) {
        double sumaMontosInvertidos = 0.0;
        double gananciasConComision = 0.0;

        for (Inversion inversion : lista) {
            sumaMontosInvertidos += inversion.getMontoInvertido();
            if (inversion.getActivo() != null) {
                double precioActual = inversion.getActivo().getPrecioActual();
                double precioInversion = inversion.getPrecioInversion();
                double comisionBroker = inversion.getBroker().getComisionPorcentaje();

                double cantidadComprada = inversion.getMontoInvertido() / precioInversion;
                double gananciaGenerada = precioActual * cantidadComprada - inversion.getMontoInvertido();
                gananciasConComision += gananciaGenerada - (gananciaGenerada * comisionBroker);
            }
        }
        return new double[]{sumaMontosInvertidos, gananciasConComision};
    }
}
