package com.filter.logging.resources;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.filter.logging.dto.AnswerWrapperDTO;
import com.filter.logging.dto.TestDTO;
import com.filter.logging.services.TestService;
import com.filter.logging.utility.ExportCsvUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test2")
@RequiredArgsConstructor
public class TestResource2 {
	
	private final TestService service;
	
	@GetMapping
	public ResponseEntity<List<TestDTO>> findAll() {
		List<TestDTO> result = service.findAll();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/byte")
	public ResponseEntity<byte[]> findByteAll() throws Exception {
		List<TestDTO> result = service.findAll();
		
		LinkedHashMap<String, String> columns = new LinkedHashMap<>();

		columns.put("id", "ID");
		columns.put("name", "NOME");
		
		byte[] byteResult = ExportCsvUtils.esportaTabellaReportFromObject(result, columns);
		
		return new ResponseEntity<>(byteResult, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TestDTO> findById(@PathVariable("id") Integer id) throws IOException {
		TestDTO result = service.findById(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/byte/{id}")
	public ResponseEntity<TestDTO> findByteById(@PathVariable("id") Integer id) throws IOException {
		TestDTO result = service.findById(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/wrapper/{id}")
	public AnswerWrapperDTO<TestDTO> findByIdWrapper(@PathVariable("id") Integer id, @RequestParam("favone") String favone) {
		TestDTO result = service.findById(id);
		return new AnswerWrapperDTO<>(result);
	}
	
	@GetMapping("/wrapper")
	public AnswerWrapperDTO<List<TestDTO>> findAllWrapper(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "cazzo", required = false) String cazzo) {
		List<TestDTO> result = service.findAll();
		return new AnswerWrapperDTO<>(result);
	}
	
	@GetMapping("/some/{id}")
	public TestDTO findBySome(@PathVariable("id") Integer id) {
		TestDTO result = service.findById(id);
		return result;
	}
	
	@GetMapping("/some")
	public List<TestDTO> findAllSome() {
		List<TestDTO> result = service.findAll();
		return result;
	}

}
