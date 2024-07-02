package com.filter.logging.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.filter.logging.dto.TestDTO;

@Service
public class TestService {

	public TestDTO findById(Integer id) {
		return new TestDTO(1, "ciao");
	}
	
	public List<TestDTO> findAll() {
		List<TestDTO> list = Arrays.asList(new TestDTO(1, "ciao"), new TestDTO(2, "fava") );
		return list;
	}

}
