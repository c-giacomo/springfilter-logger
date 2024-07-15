package com.filter.logging.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

	public byte[] createXlsx() throws IOException {
		List<TestDTO> lista = findAll();
	
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		
		List<String> testata = new ArrayList<>(Arrays.asList("ID", "NOME"));
		
		Row titoli = sheet.createRow((short) 0);

		for (int i = 0; i < testata.size(); i++) {
			Cell intestazione = titoli.createCell(i);
			intestazione.setCellValue(testata.get(i));
		}
		
		int index = 1;
		for (TestDTO item : lista) {
			Row corpo = sheet.createRow((short) index);
			
			Cell value = corpo.createCell(0);
			value.setCellValue(item.getId());
			
			value = corpo.createCell(1);
			value.setCellValue(item.getName());
			
			index++;
			
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		bos.flush();
		wb.close();
		return bos.toByteArray();
	}

}
