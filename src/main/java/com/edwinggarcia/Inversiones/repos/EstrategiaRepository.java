package com.edwinggarcia.Inversiones.repos;

import com.edwinggarcia.Inversiones.model.Estrategia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstrategiaRepository extends JpaRepository<Estrategia, Long>{
    Estrategia findByNombre(String nombre);
}

