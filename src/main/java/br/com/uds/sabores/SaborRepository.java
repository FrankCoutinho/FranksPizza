package br.com.uds.sabores;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaborRepository extends JpaRepository<Sabor, Long>
{
	@Transactional
	public Optional<Sabor> findByNomeIgnoreCase(String nome);
}
