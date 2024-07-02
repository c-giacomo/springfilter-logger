package com.filter.logging.dto;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HeaderDTO {
	
	private Date orario;

	private HttpStatus stato;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private ErrorDTO errore;
		
	public HeaderDTO () {
		this.orario = new Date();
		this.stato(HttpStatus.OK);
	}
	
	public HeaderDTO (HttpStatus stato) {
		this.orario = new Date();
		this.stato(stato);
	}
	
	public HeaderDTO stato(HttpStatus stato) {
		this.stato = stato;
		return this;
	}
	
	public HeaderDTO errore(Throwable t) {
		this.errore = new ErrorDTO(t);
		return this;
	}
	
	public HeaderDTO errore(Class<?> type, String... messages) {
		this.errore = new ErrorDTO(type, messages);
		return this;
	}
}
