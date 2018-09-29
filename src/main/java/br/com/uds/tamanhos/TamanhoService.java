package br.com.uds.tamanhos;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TamanhoService
{
	@Autowired
	private TamanhoRepository repository;

	public List<Tamanho> obterTodos()
	{
		return repository.findAll();
	}
	
	public Tamanho salvar(Tamanho tamanho)
	{
		if (repository.findByNomeIgnoreCase(tamanho.getNome()).isPresent())
			throw new EntityExistsException("O tamanho de pizza já está cadastrado");
				
		return repository.save(tamanho);
	}

	public void excluir(Tamanho tamanho)
	{
		Optional<Tamanho> resultado = repository.findByNomeIgnoreCase(tamanho.getNome());
		
		if (!resultado.isPresent())
			throw new EntityNotFoundException("O tamanho de pizza não existe");
		
		repository.delete(resultado.get());
	}
}
