package com.filter.logging.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.filter.logging.bean.GenericLoggingPojo;
import com.filter.logging.filter.interfaces.FilterInfoBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "garantegeneric")
public class LoggingFilter extends OncePerRequestFilter {

	private final FilterInfoBuilder builder;
	
	public LoggingFilter(FilterInfoBuilder builder) {
		super();
		this.builder = builder;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;
        
        Method method = null;
        try {
			method = builder.getMethodInfoS(request);
		} catch (Exception e) {
			log.error("Impossibile recuperare le informazioni del metodo chiamato");
		}
        
        Object requestBody = builder.bodyConversion(requestWrapper.getContentAsByteArray());
        Object responseBody = builder.bodyConversion(responseWrapper.getContentAsByteArray());
        
        if (requestBody.equals(""))
        	requestBody = builder.resolveQueryString(method, request);
        
        GenericLoggingPojo pojo = builder.buildPojo(request, response, requestBody, responseBody, method, timeTaken);
        
        log.info(pojo.toString());
        
        responseWrapper.copyBodyToResponse();
    }

}
