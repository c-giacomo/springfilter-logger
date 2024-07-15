package com.filter.logging.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filter.logging.bean.LoggingPojo;
import com.filter.logging.filter.interfaces.FilterInfoBuilder;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class GenericFilterInfoBuilder implements FilterInfoBuilder {
	
	protected final ObjectMapper mapper;
	protected final DispatcherServlet dispatcher;

	public GenericFilterInfoBuilder(ObjectMapper mapper, DispatcherServlet dispatcher) {
		super();
		this.mapper = mapper;
		this.dispatcher = dispatcher;
	}

	public Method getMethodInfoS(HttpServletRequest request) throws Exception {

		ServletRequestPathUtils.parseAndCache(request);

		List<HandlerMapping> mapping = dispatcher.getHandlerMappings();
		if (Objects.isNull(mapping))
			throw new ValidationException("Impossibile estrarre gli handler dal dispatcher servlet");

		HandlerMapping hEc = mapping.stream().filter(i -> i.getClass().equals(RequestMappingHandlerMapping.class)).findAny().orElseThrow(Exception::new);
		HandlerExecutionChain handlerChain = hEc.getHandler(request);
		if (Objects.isNull(handlerChain))
			throw new ValidationException("Impossibile recuperare l'handle della richiesta");

		HandlerMethod handlerMethod = (HandlerMethod) handlerChain.getHandler();
		return handlerMethod.getMethod();
	}

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

	protected Object getListObject(byte[] body) {
		try {
			List<Map<String, Object>> result = mapper.readValue(body, new TypeReference<List<Map<String, Object>>>() {});
			return transformArray(result);
		} catch (IOException e) {
			log.error("Impossibile decodificare il contenuto del messaggio in ingresso/uscita", e);
			return "";
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object decode(Map<String, Object> map) {
		try {
			List result = (ArrayList) map.get("data");
			if (Objects.isNull(result)) return map.toString();
			return transformArray(result);
		} catch (Exception e) {
			Map<String, Object> result = (Map<String, Object>) map.get("data");
			return result.toString();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object transformArray(List l) {
		List<Integer> res = new ArrayList<>();
		l.forEach(item -> {
			LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) item;
			res.add((Integer) result.get("id"));
		});
		return res;
	}

	public Object resolveQueryString(Method method, HttpServletRequest request) {
		try {
			Parameter[] reqParam = method.getParameters();
			if (reqParam.length == 0) return resolvePathVariable(request, null);
			List<Parameter> list = Arrays.stream(reqParam).collect(Collectors.toList());
			Map<String, Object> map = list.stream().filter(p -> request.getParameter(p.getName()) != null).collect(Collectors.toMap(Parameter::getName, p -> request.getParameter(p.getName())));
			return resolvePathVariable(request, map);
		} catch (Exception e) {
			log.debug("Nessun requestparam presente, vaglio i pathvariable..");
			return resolvePathVariable(request, null);
		}
	}

	protected Object resolvePathVariable(HttpServletRequest request, Map<String, Object> map) {
		Object result = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (map != null && !map.isEmpty())
			return result.toString().concat(map.toString());
		else
			return result;
	}
	
	public LoggingPojo buildPojo(HttpServletRequest request, HttpServletResponse response, Object requestBody, Object responseBody, Method method, long timeTaken) {
		LoggingPojo pojo = new LoggingPojo();
        return pojo.build(request, response, requestBody, responseBody, method, timeTaken);
	}

}
