package com.cadastro.dinossauro.repository;

import com.cadastro.dinossauro.model.Dinossauro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DinossauroRepository extends JpaRepository<Dinossauro, Long> {
}
