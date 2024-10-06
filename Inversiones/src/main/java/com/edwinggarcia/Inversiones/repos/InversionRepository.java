package com.edwinggarcia.Inversiones.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edwinggarcia.Inversiones.model.Inversion;

@Repository
public interface InversionRepository extends JpaRepository<Inversion, Long> {
}

