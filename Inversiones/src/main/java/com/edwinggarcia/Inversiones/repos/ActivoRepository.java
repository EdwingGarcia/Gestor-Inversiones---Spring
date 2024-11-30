package com.edwinggarcia.Inversiones.repos;

import com.edwinggarcia.Inversiones.model.Activo;
import com.edwinggarcia.Inversiones.model.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivoRepository  extends JpaRepository<Activo, Long> {
    Activo findBySimbolo(String nombre);
    List<Activo> findByTipo(String tipo);

    @Query("SELECT DISTINCT a.tipo FROM Activo a")
    List<String> findDistinctTipos();


}

