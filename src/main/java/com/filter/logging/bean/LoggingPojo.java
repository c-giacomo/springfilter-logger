package com.filter.logging.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class LoggingPojo {
	
	private String requestType;
	private String requestUri;
	private String requestHost;
	private Object requestPayload;
	private int responseCode;
	private Object responsePayload;
	private long timeTaken;
	private String methodSignature;
	private String returnType;
	private String returnObject;
	
	@SuppressWarnings("rawtypes")
	public LoggingPojo build(HttpServletRequest request, HttpServletResponse response, Object requestBody, Object responseBody, Method method, long timeTaken) {
		this.setRequestType(request.getMethod());
		this.setRequestUri(request.getRequestURI());
		this.setRequestHost(request.getRequestURL().toString());
		if (requestBody instanceof ArrayList)
			this.setRequestPayload((List)requestBody);
		else
			this.setRequestPayload(requestBody.toString());
		this.setResponseCode(response.getStatus());
		
		if (responseBody instanceof ArrayList)
			this.setResponsePayload((List)responseBody);
		else
			this.setResponsePayload(responseBody.toString());
		
		this.setTimeTaken(timeTaken);
		this.setMethodSignature(method.toString());
		this.setReturnType(method.getReturnType().getTypeName());
		
		String returnObj = getSignature(method);
		if (returnObj != null)
			this.setReturnObject(returnObj);
		return this;
	}
	
	private String getSignature(Method method) {
		String sig = null;
		
		try {
			Field signature = Method.class.getDeclaredField("signature");
			signature.setAccessible(true);
			sig = (String) signature.get(method);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			log.debug("Impossibile recuperare la signature del metodo ", e);
		}
		
		return parseSignature(sig);
	}
	
	private String parseSignature(String sig) {
		if (sig == null) return null;
		return sig.replaceAll("\\(([^\\)].*?)\\)(L)|(/)|(;>)|(<L)|(<)", "."); // lazy regex
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FINISHED PROCESSING : REQUESTTYPE=");
		builder.append(requestType);
		builder.append(", REQUESTURI=");
		builder.append(requestUri);
		builder.append(", REQUESTHOST=");
		builder.append(requestHost);
		builder.append(", REQUESTPAYLOAD=");
		builder.append(requestPayload);
		builder.append(", RESPONSECODE=");
		builder.append(responseCode);
		builder.append(", RESPONSEPAYLOAD=");
		builder.append(responsePayload);
		builder.append(", TIMETAKEN=");
		builder.append(timeTaken);
		builder.append(", METHODSIGNATURE=");
		builder.append(methodSignature);
		builder.append(", RETURNTYPE=");
		builder.append(returnType);
		if (returnObject != null) {
			builder.append(", RETURNOBJECT=");
			builder.append(returnObject);
		}
		
		return builder.toString();
	}

	

}
