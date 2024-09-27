package com.edwingGarcia.CRUD.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edwingGarcia.CRUD.model.Inversion;

@Repository
public interface InversionRepository extends JpaRepository<Inversion, Long> {
}
