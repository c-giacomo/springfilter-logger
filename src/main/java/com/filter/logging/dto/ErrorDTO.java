package com.filter.logging.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * Oggetto non modificabile per il mapping degli errori del servizio rest
 * 
 * @author mbiviglia
 *
 */
@Getter
@ToString
public class ErrorDTO {
	
	private String tipo;
	private String[] messaggi;
	
	public ErrorDTO (Class<?> type, String... messages){
		super();
		this.tipo = type.getSimpleName();
		this.messaggi = messages;
	}
	
	public ErrorDTO (Throwable t){
		this(t.getClass(), t.getMessage());
	}
}
