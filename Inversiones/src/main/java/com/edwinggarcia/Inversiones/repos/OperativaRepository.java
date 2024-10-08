package com.edwinggarcia.Inversiones.repos;

import com.edwinggarcia.Inversiones.model.Operativa;
import com.edwinggarcia.Inversiones.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperativaRepository  extends JpaRepository<Operativa,Long> {
}
