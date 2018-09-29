package br.com.uds.sabores;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaborService
{
	@Autowired
	private SaborRepository repository;

	public List<Sabor> obterTodos()
	{
		return repository.findAll();
	}
	
	public Sabor salvar(Sabor sabor)
	{
		if (repository.findByNomeIgnoreCase(sabor.getNome()).isPresent())
			throw new EntityExistsException("O sabor de pizza já está cadastrado");
				
		return repository.save(sabor);
	}

	public void excluir(Sabor sabor)
	{
		Optional<Sabor> resultado = repository.findByNomeIgnoreCase(sabor.getNome());
		
		if (!resultado.isPresent())
			throw new EntityNotFoundException("O sabor de pizza não existe");
		
		repository.delete(resultado.get());
	}
}
