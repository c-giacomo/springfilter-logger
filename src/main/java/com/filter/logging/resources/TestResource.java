package com.filter.logging.resources;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filter.logging.dto.AnswerWrapperDTO;
import com.filter.logging.dto.TestDTO;
import com.filter.logging.services.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestResource {
	
	private final TestService service;
	
	@GetMapping
	public ResponseEntity<List<TestDTO>> findAll() {
		List<TestDTO> result = service.findAll();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TestDTO> findById(@PathVariable("id") Integer id) {
		TestDTO result = service.findById(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/{fava}")
	public ResponseEntity<TestDTO> findByIdAndId(@PathVariable("id") Integer id, @PathVariable("fava") String fava) {
		TestDTO result = service.findById(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/wrapper/{id}")
	public AnswerWrapperDTO<TestDTO> findByIdWrapper(@PathVariable("id") Integer id) {
		TestDTO result = service.findById(id);
		return new AnswerWrapperDTO<>(result);
	}
	
	@GetMapping("/wrapper")
	public AnswerWrapperDTO<List<TestDTO>> findAllWrapper() {
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
	
	@GetMapping("/xlsx")
	public ResponseEntity<byte[]> buildResponseFile() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=prova.xlsx");
		
		byte[] file = service.createXlsx();
		
		return ResponseEntity.ok()
				   .headers(headers)
				   .contentType(MediaType.parseMediaType("application/octet-stream"))
				   .body(file);
    }
}
