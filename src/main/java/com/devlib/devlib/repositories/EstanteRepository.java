package com.devlib.devlib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlib.devlib.entites.Estante;

@Repository
public interface EstanteRepository extends JpaRepository<Estante, Long> {

}
