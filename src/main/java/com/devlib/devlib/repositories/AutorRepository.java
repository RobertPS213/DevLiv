package com.devlib.devlib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlib.devlib.entites.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

}
