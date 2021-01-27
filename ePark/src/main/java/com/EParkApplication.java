package com;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.github.mjstewart.querystring.dialect.QueryStringDialect;

@EnableWebSecurity
@SpringBootApplication
public class EParkApplication {

	public static void main(String[] args) {
		SpringApplication.run(EParkApplication.class, args);
		
	}
	
	@Bean
	public QueryStringDialect queryStringDialect() {
		return new QueryStringDialect();
	}
	
	  @PostConstruct
	  void started() {
	    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	  }
}
