package com.filter.logging.utility;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.time.FastDateFormat;

public interface DateFormatter {
	
	public static FastDateFormat nowYear = FastDateFormat.getInstance("yyyy");

	public static FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");

	public static FastDateFormat yyyyMMdd = FastDateFormat.getInstance("yyyyMMdd");

	public static FastDateFormat hhMMssSSS = FastDateFormat.getInstance("HH:mm:ss.SSS");

	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public static FastDateFormat localeIta = FastDateFormat.getInstance("dd/MM/yyyy");

	public static FastDateFormat dateTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public static FastDateFormat dateInFilename = FastDateFormat.getInstance("yyyyMMdd_HHmmss");

	public static FastDateFormat MMyyyy = FastDateFormat.getInstance("MM/yyyy");
	
	public static class DateFormatterAdapter extends XmlAdapter<String, Date> {
		private final FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");

		@Override
		public Date unmarshal(final String v) throws Exception {
			return dateFormat.parse(v);
		}

		@Override
		public String marshal(final Date v) throws Exception {
			return dateFormat.format(v);
		}
	}
		
}
