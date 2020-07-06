package com.geektime.springmvc1;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.geektime.springmvc1.controller.PerformanceInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
@EnableDiscoveryClient
public class Springmvc1Application implements WebMvcConfigurer, WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	public static void main(String[] args) {
		SpringApplication.run(Springmvc1Application.class, args);
	}

	@Bean
	public Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
		return jacksonObjectMapperBuilder -> {
			jacksonObjectMapperBuilder.indentOutput(true);
			jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		};
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PerformanceInterceptor())
				.addPathPatterns("/coffee/**").addPathPatterns("/order/**");
	}

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
//		Compression compression = new Compression();
//		compression.setEnabled(true);
//		compression.setMinResponseSize(DataSize.ofBytes(512));
//		factory.setCompression(compression);
	}
}
