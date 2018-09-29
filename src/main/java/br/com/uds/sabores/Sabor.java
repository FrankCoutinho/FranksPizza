
package br.com.uds.sabores;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Sabor
{
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 255)
	private String nome;

	@PositiveOrZero
	private int tempoAdicional;

	/* CONSTRUTORES */
	
	public Sabor()
	{
	}
	
	public Sabor(String nome, int tempoAdicional)
	{
		this.nome = nome;
		this.tempoAdicional = tempoAdicional;
	}
	
	/* MÉTODOS GET */

	public Long getId()
	{
		return id;
	}
	
	public String getNome()
	{
		return nome;
	}

	public int getTempoAdicional()
	{
		return tempoAdicional;
	}

	/* MÉTODOS SET */

	public void setId(Long id)
	{
		this.id = id;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public void setTempoAdicional(int tempoAdicional)
	{
		this.tempoAdicional = tempoAdicional;
	}


	/* MÉTODOS SOBRESCRITOS */
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public String toString()
	{
		return "Sabor [id=" + id + ", nome=" + nome + ", tempoAdicional=" + tempoAdicional + "]";
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (this == object)
			return true;
		
		if (object == null)
			return false;
		
		if (getClass() != object.getClass())
			return false;
		
		Sabor other = (Sabor) object;
		
		if (nome == null)
		{
			if (other.nome != null)
				return false;
		} 
		else
			if (!nome.equals(other.nome))
				return false;
		
		return true;
	}
}
