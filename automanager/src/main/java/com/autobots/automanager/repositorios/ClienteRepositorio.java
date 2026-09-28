package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

import com.autobots.automanager.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	@EntityGraph(attributePaths = {"documentos", "endereco", "telefones"})
	@Override
	List<Cliente> findAll();

	@EntityGraph(attributePaths = {"documentos", "endereco", "telefones"})
	@Override
	Optional<Cliente> findById(Long id);
}
