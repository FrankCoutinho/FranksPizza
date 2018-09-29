
package br.com.uds.tamanhos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Tamanho
{
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@PositiveOrZero
	private double custo;

	@NotNull
	@Size(min = 3, max = 255)
	private String nome;

	@PositiveOrZero
	private int tempoDePreparo;

	/* CONSTRUTORES */

	public Tamanho()
	{
	}

	public Tamanho(@NotNull @Size(min = 3, max = 255) String nome, @PositiveOrZero int tempoDePreparo, @PositiveOrZero double custo)
	{
		super();
		this.nome = nome;
		this.custo = custo;
		this.tempoDePreparo = tempoDePreparo;
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

	public double getCusto()
	{
		return custo;
	}

	public int getTempoDePreparo()
	{
		return tempoDePreparo;
	}

	/* MÉTODOS SET */

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setNome(String nome)
	{
		this.nome = nome.trim();
	}

	public void setCusto(double custo)
	{
		this.custo = custo;
	}

	public void setTempoDePreparo(int tempoDePreparo)
	{
		this.tempoDePreparo = tempoDePreparo;
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
		return "Tamanho [custo=" + custo + ", nome=" + nome + ", tempoDePreparo=" + tempoDePreparo + "]";
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
		
		Tamanho other = (Tamanho) object;
		
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
