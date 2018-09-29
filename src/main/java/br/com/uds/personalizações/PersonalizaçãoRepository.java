package br.com.uds.personalizações;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalizaçãoRepository extends JpaRepository<Personalização, Long>
{
	@Transactional
	public Optional<Personalização> findByDescriçãoIgnoreCase(String descrição);
}
