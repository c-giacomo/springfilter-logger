package com.filter.logging.filter.interfaces;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filter.logging.bean.GenericLoggingPojo;

public interface FilterInfoBuilder {

	public Method getMethodInfoS(HttpServletRequest request) throws Exception;

	public Object bodyConversion(byte[] contentAsByteArray);

	public Object resolveQueryString(Method method, HttpServletRequest request);
	
	public GenericLoggingPojo buildPojo(HttpServletRequest request, HttpServletResponse response, Object requestBody, Object responseBody, Method method, long timeTaken);

}
