package com.devlib.devlib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlib.devlib.entites.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
