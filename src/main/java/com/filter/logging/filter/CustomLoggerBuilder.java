package com.filter.logging.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Primary
@Profile("custom")
public class CustomLoggerBuilder extends GenericLoggerBuilder {
	
	public CustomLoggerBuilder(ObjectMapper mapper, DispatcherServlet dispatcher) {
		super(mapper, dispatcher);
	}
	
	@Override
	public Object bodyConversion(byte[] body) {
		if (body.length == 0) return "";
		else {
			try {
				Map<String, Object> result = mapper.readValue(body, new TypeReference<Map<String, Object>>() {});
				return decode(result);
			} catch (IOException e) {
				return getListObject(body);
			}
		}
	}

	@Override
	protected Object getListObject(byte[] body) {
		try {
			List<Map<String, Object>> result = mapper.readValue(body, new TypeReference<List<Map<String, Object>>>() {});
			return transformArray(result);
		} catch (IOException e) {
			if (isXLSX(body)) return getXlsxObject(body);
			else return getCsvObject(body);
		}
	}
	
	@SuppressWarnings("resource")
	protected Object getXlsxObject(byte[] body) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
			XSSFWorkbook input = new XSSFWorkbook(inputStream);
	
			String tsv = new XSSFExcelExtractor(input).getText();
			BufferedReader reader = new BufferedReader(new StringReader(tsv));
			
//			String line;
//			StringBuilder builder = new StringBuilder();
//			while ((line = reader.readLine()) != null) {
//                builder.append(line);
//                builder.append("\n");
//            }
			
			return (reader.lines().count() - 2);
		} catch (Exception e) {
			log.error("",e);
			return "";
		}
	}
	
	
	protected Object getCsvObject(byte[] body) {
		try {
			return new String(body, StandardCharsets.UTF_8);
		} catch (Exception e) {
			return "";
		}
	}

    private static boolean isXLSX(byte[] fileBytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
            ZipInputStream zis = new ZipInputStream(bais)) {
            return zis.getNextEntry() != null;
        } catch (IOException e) {
            return false;
        }
    }


}
