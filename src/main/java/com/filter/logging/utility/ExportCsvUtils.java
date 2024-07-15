package com.filter.logging.utility;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"unchecked", "rawtypes", "unused"})
@Slf4j
public class ExportCsvUtils {
	
	/**
	 * Questo metodo viene utilizzato solo dai test. 
	 * è un metodo equivalente a {@link ExportCsvUtils#esportaTabellaReportFromObject(List, LinkedHashMap)}, 
	 * ma invece che restituire un byte[], restituisce una stringa, che può essere confrontata. 
	 * @author lchianella
	 */
	public static <T> String esportaCsvForTests(List<T> tabella, LinkedHashMap<String, String> mappingColumns) throws Exception {

		try (StringWriter sw = new StringWriter()) {
			CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT.withDelimiter(';'));
				

			if(tabella.isEmpty()) {
				csvPrinter.printRecord("Non ci sono dati disponibili per i parametri selezionati.");
			} else {
				List header = new ArrayList();
				for (String fieldName : mappingColumns.keySet()) {
					header.add(mappingColumns.get(fieldName));
				}
				csvPrinter.printRecord(header);
				
				Class<? extends Object> classz = tabella.get(0).getClass();
				
				List row = null;
				for (T t : tabella) {
					row = new ArrayList();
					for (String fieldName : mappingColumns.keySet()) {
						Method method = null;
						try {
							method = classz.getMethod("get" + capitalize(fieldName));
						} catch (NoSuchMethodException nme) {
							method = classz.getMethod("get" + fieldName);
						}
						Object value = method.invoke(t, (Object[]) null);
						if (value != null) {
							if (value instanceof String) {
								row.add((String) value);
							} else if (value instanceof Long) {
								if(value != null) row.add((Long) value);							
								else row.add(null);
							} else if (value instanceof Integer) {
								if(value != null) row.add((Integer) value);						
								else row.add(null);
							} else if (value instanceof BigDecimal) {
								if(value != null) {
									double doubleVal = ((BigDecimal) value).doubleValue(); 
									Locale itLocale  = new Locale("it", "IT");
									DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(itLocale);
									df.applyPattern("#,##0.00");
									String stringValue = df.format(doubleVal);
									row.add(stringValue);
								} else row.add(null);
						 	  
						    } else if (value instanceof Double) {
						    	if(value != null) row.add((Double) value);
								else row.add(null);
							} else if (value instanceof Date) {
								if(value != null) {
							        String stringValue = DateFormatter.localeIta.format(value);
									row.add(stringValue);
								} else row.add(null);
							} else if (value instanceof Boolean) {
								if(value != null) row.add(value);
								else row.add(null);
							}
						} else row.add(null);
					}
				csvPrinter.printRecord(row);
				}
			}
			
		    if (csvPrinter != null) {
		    	csvPrinter.flush();
		        sw.flush();
		    }
		    csvPrinter.close();
	        return sw.toString();
	  	} catch (Exception e) {
	  		throw e;
	  	}
    }
	
	public static <T> byte[] esportaTabellaReportFromObject(List<T> tabella, LinkedHashMap<String, String> mappingColumns) throws Exception {

		try (StringWriter sw = new StringWriter()) {
			CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT.withDelimiter(';'));
				

			if(tabella.isEmpty()) {
				csvPrinter.printRecord("Non ci sono dati disponibili per i parametri selezionati.");
			} else {
				List header = new ArrayList();
				for (String fieldName : mappingColumns.keySet()) {
					header.add(mappingColumns.get(fieldName));
				}
				csvPrinter.printRecord(header);
				
				Class<? extends Object> classz = tabella.get(0).getClass();
				
				List row = null;
				for (T t : tabella) {
					row = new ArrayList();
					for (String fieldName : mappingColumns.keySet()) {
						Method method = null;
						try {
							method = classz.getMethod("get" + capitalize(fieldName));
						} catch (NoSuchMethodException nme) {
							method = classz.getMethod("get" + fieldName);
						}
						Object value = method.invoke(t, (Object[]) null);
						if (value != null) {
							if (value instanceof String) {
								row.add((String) value);
							} else if (value instanceof Long) {
								if(value != null) row.add((Long) value);							
								else row.add(null);
							} else if (value instanceof Integer) {
								if(value != null) row.add((Integer) value);						
								else row.add(null);
							} else if (value instanceof BigDecimal) {
								if(value != null) {
									double doubleVal = ((BigDecimal) value).doubleValue(); 
									Locale itLocale  = new Locale("it", "IT");
									DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(itLocale);
									df.applyPattern("#,##0.00");
									String stringValue = df.format(doubleVal);
									row.add(stringValue);
								} else row.add(null);
						 	  
						    } else if (value instanceof Double) {
						    	if(value != null) row.add((Double) value);
								else row.add(null);
							} else if (value instanceof Date) {
								if(value != null) {
							        String stringValue = DateFormatter.localeIta.format(value);
									row.add(stringValue);
								} else row.add(null);
							} else if (value instanceof Boolean) {
								if(value != null) row.add(value);
								else row.add(null);
							}
						} else row.add(null);
					}
				csvPrinter.printRecord(row);
				}
			}
			
		    if (csvPrinter != null) {
		    	csvPrinter.flush();
		        sw.flush();
		    }
		    csvPrinter.close();
	        return sw.toString().getBytes();
	  	} catch (Exception e) {
	  		throw e;
	  	}
    }
	
	/**
	 * Metodo utilizzato esclusivamente e ad hoc per il report monitoraggio
	 * @param tabella
	 * @param mappingColumns
	 * @return
	 */
	
	private static String capitalize(String s) {
		if (s.length() == 0) return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

}
