package com.filter.logging.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.filter.logging.filter.LoggingFilter;
import com.filter.logging.filter.interfaces.FilterInfoBuilder;

@Configuration
public class FilterConfig {
	
	@Value("${paths}")
	private String[] paths;
	
	@Bean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	FilterRegistrationBean<LoggingFilter> logFilter(FilterInfoBuilder builder) {
		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoggingFilter(builder));
		registrationBean.addUrlPatterns(paths);
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registrationBean;
	}

}
