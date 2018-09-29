
package br.com.uds.pedidos;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.uds.personalizações.Personalização;
import br.com.uds.sabores.Sabor;
import br.com.uds.tamanhos.Tamanho;

@Entity
public class Pedido
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Valid
	@NotNull
	@ManyToOne
	@JoinColumn
	private Sabor sabor;

	@Valid
	@NotNull
	@ManyToOne
	@JoinColumn
	private Tamanho tamanho;

	@Transient
	private double valorTotal;

	@Transient
	private int tempoDePreparo;

	@Valid
	@JoinTable
	@ManyToMany
	private Set<Personalização> personalizações;

	/* CONSTRUTORES */

	protected Pedido()
	{
	}

	public Pedido(Sabor sabor, Tamanho tamanho, Personalização... personalizações)
	{
		this.sabor = sabor;
		this.tamanho = tamanho;
		this.personalizações = new HashSet<>(Arrays.asList(personalizações));
	}

	/* MÉTODOS GET */

	public Long getId()
	{
		return id;
	}

	public Sabor getSabor()
	{
		return sabor;
	}

	public Tamanho getTamanho()
	{
		return tamanho;
	}

	public double getValorTotal()
	{
		return valorTotal;
	}

	public int getTempoDePreparo()
	{
		return tempoDePreparo;
	}

	public Set<Personalização> getPersonalizações()
	{
		return personalizações;
	}

	/* MÉTODOS SET */

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setSabor(Sabor sabor)
	{
		this.sabor = sabor;
	}

	public void setTamanho(Tamanho tamanho)
	{
		this.tamanho = tamanho;
	}

	public void setValorTotal(double valorTotal)
	{
		this.valorTotal = valorTotal;
	}

	public void setTempoDePreparo(int tempoDePreparo)
	{
		this.tempoDePreparo = tempoDePreparo;
	}

	public void setPersonalizações(Set<Personalização> personalizações)
	{
		this.personalizações = personalizações;
	}

	/* MÉTODOS SOBRESCRITOS */

	@Override
	public String toString()
	{
		return "Pedido [id=" + id + ", sabor=" + sabor + ", tamanho=" + tamanho + ", valorTotal=" + valorTotal + ", tempoDePreparo=" + tempoDePreparo
				+ ", personalizações=" + personalizações + "]";
	}

	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
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
		
		Pedido other = (Pedido) object;
		
		if (id == null)
		{
			if (other.id != null)
				return false;
		} 
		else
			if (!id.equals(other.id))
				return false;
		
		return true;
	}
	
	/* MÉTODOS PERSONALIZADOS */

	public int calcularTempoDePreparo()
	{
		this.tempoDePreparo = 0;
		this.tempoDePreparo += sabor.getTempoAdicional();
		this.tempoDePreparo += tamanho.getTempoDePreparo();
		this.tempoDePreparo += personalizações.stream()
											  .mapToInt(personalização -> personalização.getTempoAdicional())
											  .sum();
		
		return this.tempoDePreparo;
	}

	public double calcularValorTotal()
	{
		this.valorTotal = 0;
		this.valorTotal += tamanho.getCusto();
		this.valorTotal += personalizações.stream()
				  						  .mapToDouble(personalização -> personalização.getCustoAdicional())
				  						  .sum();
		
		return this.valorTotal;
	}
}
