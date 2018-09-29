package br.com.uds.personalizações;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalizaçãoService
{
	@Autowired
	private PersonalizaçãoRepository repository;

	public List<Personalização> obterTodos()
	{
		return repository.findAll();
	}
	
	public Personalização salvar(Personalização personalização)
	{
		if (repository.findByDescriçãoIgnoreCase(personalização.getDescrição()).isPresent())
			throw new EntityExistsException("A personalização já está cadastrado");
				
		return repository.save(personalização);
	}

	public void excluir(Personalização personalização)
	{
		Optional<Personalização> resultado = repository.findByDescriçãoIgnoreCase(personalização.getDescrição());
		
		if (!resultado.isPresent())
			throw new EntityNotFoundException("A personalização não existe");
		
		repository.delete(resultado.get());
	}
}
