package com.edwinggarcia.Inversiones.service;

import com.edwinggarcia.Inversiones.model.Activo;
import com.edwinggarcia.Inversiones.model.Broker;
import com.edwinggarcia.Inversiones.model.Estrategia;
import com.edwinggarcia.Inversiones.repos.ActivoRepository;
import com.edwinggarcia.Inversiones.repos.BrokerRepository;
import com.edwinggarcia.Inversiones.repos.EstrategiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenciaService {
    private final BrokerRepository brokerRepository;
    private final ActivoRepository activoRepository;
    private final EstrategiaRepository estrategiaRepository;

    @Autowired
    public ReferenciaService(BrokerRepository brokerRepository, ActivoRepository activoRepository, EstrategiaRepository estrategiaRepository) {
        this.brokerRepository = brokerRepository;
        this.activoRepository = activoRepository;
        this.estrategiaRepository = estrategiaRepository;
    }

    public List<Broker> getAllBrokers() {
        return brokerRepository.findAll();
    }

    public List<Estrategia> getAllEstrategias() {
        return estrategiaRepository.findAll();
    }

    public List<Activo> getAllActivosDisponibles() {
        return activoRepository.findAll();
    }
}