package br.com.uds.tamanhos;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TamanhoRepository extends JpaRepository<Tamanho, Long>
{
	@Transactional
	public Optional<Tamanho> findByNomeIgnoreCase(String nome);
}
