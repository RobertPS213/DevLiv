package com.devlib.devlib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlib.devlib.entites.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

	boolean existsByAutoresId(Long id);
	boolean existsByCategoriasId(Long id);
	boolean existsByEditoraId(Long id);
}
