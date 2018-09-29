package br.com.uds.personalizações;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Personalização
{
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 255)
	private String descrição;
	
	@PositiveOrZero
	private int tempoAdicional;
	
	@PositiveOrZero
	private double custoAdicional;

	/* CONSTRUTORES */
	
	public Personalização()
	{
		
	}
	
	public Personalização(String descrição, int tempoAdicional, double custoAdicional)
	{
		this.descrição = descrição;
		this.tempoAdicional = tempoAdicional;
		this.custoAdicional = custoAdicional;
	}
	
	/* MÉTODOS GET */
	
	public Long getId()
	{
		return id;
	}

	public String getDescrição()
	{
		return descrição;
	}

	public int getTempoAdicional()
	{
		return tempoAdicional;
	}

	public double getCustoAdicional()
	{
		return custoAdicional;
	}
	
	/* MÉTODOS SET */
	
	public void setId(Long id)
	{
		this.id = id;
	}

	public void setDescrição(String descrição)
	{
		this.descrição = descrição;
	}

	public void setTempoAdicional(int tempoAdicional)
	{
		this.tempoAdicional = tempoAdicional;
	}

	public void setCustoAdicional(double custoAdicional)
	{
		this.custoAdicional = custoAdicional;
	}



	/* MÉTODOS SOBRESCRITOS */
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descrição == null) ? 0 : descrição.hashCode());
		return result;
	}

	@Override
	public String toString()
	{
		return "Personalização [id=" + id + ", descrição=" + descrição + ", tempoAdicional=" + tempoAdicional + ", custoAdicional=" + custoAdicional
				+ "]";
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
		
		Personalização other = (Personalização) object;
		
		if (descrição == null)
		{
			if (other.descrição != null)
				return false;
		} 
		else
			if (!descrição.equals(other.descrição))
				return false;
		return true;
	}
}
