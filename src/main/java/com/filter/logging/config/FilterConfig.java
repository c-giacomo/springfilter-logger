package com.filter.logging.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.DispatcherServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filter.logging.filter.LoggingFilter;

@Configuration
public class FilterConfig {
	
	String[] paths = new String[]{"/api/v1/test/*", "/api/v1/test2/*"};
	
	@Bean
//	@Profile("")
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	FilterRegistrationBean<LoggingFilter> logFilter(ObjectMapper mapper, DispatcherServlet dispatcher) {
		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoggingFilter(mapper, dispatcher));
		registrationBean.addUrlPatterns(paths);
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registrationBean;
	}

}
