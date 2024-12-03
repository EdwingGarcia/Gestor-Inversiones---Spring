package com.edwinggarcia.Inversiones.repos;

import com.edwinggarcia.Inversiones.model.Broker;
import com.edwinggarcia.Inversiones.model.Inversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface InversionRepository extends JpaRepository<Inversion, Long> {
    List<Inversion> findByEmailUsuario(String emailUsuario);
    List<Inversion> findByEmailUsuarioAndFechaInversionBetween(String emailUsuario, LocalDate fechaInicio, LocalDate fechaFin);
    List<Inversion> findByEmailUsuarioAndBroker(String emailUsuario, Broker broker);
    List<Inversion> findByFechaInversionBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
