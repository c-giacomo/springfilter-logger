package com.filter.logging.bean;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class CustomLoggingPojo {
	
	private String timestamp; // 20 padding di spazi a dx
	private int matricola; // 15 padding di 0 a sx
	private String ipAddress; // 15
	private String codFiscale; // 16
	private String partitaIva; // 16
	
	

}
