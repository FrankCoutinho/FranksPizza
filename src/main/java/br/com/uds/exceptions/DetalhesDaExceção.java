package br.com.uds.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class DetalhesDaExceção
{
	private String mensagem;
	private HttpStatus status;
	private LocalDateTime horário;

	/* CONSTRUTORES */
	
	public DetalhesDaExceção(String mensagem, HttpStatus status)
	{
		this.status = status;
		this.mensagem = mensagem;
		this.horário = LocalDateTime.now();
	}

	/* MÉTODOS GET */
	
	public String getMensagem()
	{
		return mensagem;
	}

	public HttpStatus getStatus()
	{
		return status;
	}

	public LocalDateTime getHorário()
	{
		return horário;
	}

	/* MÉTODOS SET */

	public void setMensagem(String mensagem)
	{
		this.mensagem = mensagem;
	}

	public void setStatus(HttpStatus status)
	{
		this.status = status;
	}

	public void setHorário(LocalDateTime horário)
	{
		this.horário = horário;
	}
}
