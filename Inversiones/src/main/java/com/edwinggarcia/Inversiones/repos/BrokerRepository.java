package com.edwinggarcia.Inversiones.repos;

import com.edwinggarcia.Inversiones.model.Broker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrokerRepository  extends JpaRepository<Broker, Long> {
    Broker findByNombre(String nombre);
}
