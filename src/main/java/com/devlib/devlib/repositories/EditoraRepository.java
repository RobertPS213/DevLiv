package com.devlib.devlib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlib.devlib.entites.Editora;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {

}
