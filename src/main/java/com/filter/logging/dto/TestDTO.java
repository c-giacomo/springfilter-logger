package com.filter.logging.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestDTO implements Serializable {
	
	private Integer id;
	private String name;
	
	
	public TestDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	

}
