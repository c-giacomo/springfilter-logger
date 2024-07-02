package com.filter.logging.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.ServletRequestPathUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filter.logging.bean.LoggingPojo;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "garantegeneric")
public class LoggingFilter extends OncePerRequestFilter {
	
	private final ObjectMapper mapper;
	private final DispatcherServlet dispatcher;
	
	public LoggingFilter(ObjectMapper mapper, DispatcherServlet dispatcher) {
		super();
		this.mapper = mapper;
		this.dispatcher = dispatcher;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;
        
        Object requestBody = bodyConversion(requestWrapper.getContentAsByteArray());
        Object responseBody = bodyConversion(responseWrapper.getContentAsByteArray());
        
        Method method = null;
        try {
			method = getMethodInfoS(request);
		} catch (Exception e) {
			log.error("Impossibile recuperare le informazioni del metodo chiamato");
		}
        
        LoggingPojo pojo = new LoggingPojo();
        pojo.build(requestWrapper, responseWrapper, requestBody, responseBody, method, timeTaken);
        
        log.info(pojo.toString());
        
        responseWrapper.copyBodyToResponse();
    }
    
    private Method getMethodInfoS(HttpServletRequest request) throws Exception {
    	
    	ServletRequestPathUtils.parseAndCache(request);
        
        List<HandlerMapping> mapping = dispatcher.getHandlerMappings();
        List<HandlerExecutionChain> chain = new ArrayList<>();
        if (Objects.isNull(mapping)) throw new ValidationException("Impossibile estrarre gli handler dal dispatcher servlet");
        mapping.forEach(item -> {
        	try {
        		HandlerExecutionChain c = item.getHandler(request);
        		chain.add(c);
			} catch (Exception e) {
				log.error("Impossibile estrarre l'iesimo handler gestito dalla richiesta", e);
			}
        });
        
        List<HandlerExecutionChain> resource = chain.stream().filter(Objects::nonNull).collect(Collectors.toList());
        HandlerExecutionChain hEc = resource.stream().findFirst().orElseThrow(() -> new ValidationException("Tutti gli handler sono null"));
        HandlerMethod handlerMethod = (HandlerMethod) hEc.getHandler();
        return handlerMethod.getMethod();
    	
    }
	
	private Object bodyConversion(byte[] body) {
		if (body.length == 0) return "";
		else {
	    	try {
				Map<String, Object> result = mapper.readValue(body, new TypeReference<Map<String, Object>>() {});
				return decode(result);
			} catch (IOException e) {
				return getWrappedObject(body);
			}
		}
    }
	
	private Object getWrappedObject(byte[] body) {
    	try {
			List<Map<String, Object>> result = mapper.readValue(body, new TypeReference<List<Map<String, Object>>>() {});
			return transformArray(result);
		} catch (IOException e) {
			log.error("Impossibile decodificare il contenuto del messaggio in ingresso/uscita", e);
			return "";
		}
    }
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object decode(Map<String, Object> map) {
		try {
			List result = (ArrayList) map.get("data");
			if (Objects.isNull(result)) return map.toString();
			return transformArray(result);
		} catch(Exception e) {
			Map<String, Object> result = (Map<String, Object>) map.get("data");
			return result.toString();
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object transformArray(List l) {
		List<Integer> res = new ArrayList<>();
		l.forEach(item -> {
			LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) item;
			res.add((Integer)result.get("id"));
		});
		return res;
	}
	
	

}
