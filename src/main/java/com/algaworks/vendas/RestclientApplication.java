package com.algaworks.vendas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.algaworks.vendas.springsample.hello.Quote;

@SpringBootApplication
public class RestclientApplication {

	private static final Logger log = LoggerFactory.getLogger(RestclientApplication.class);

	private static final String URL = "https://gturnquist-quoters.cfapps.io/api/random";

	public static void main(String[] args) {
		SpringApplication.run(RestclientApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) {		
//		CommandLineRunner runner = new CommandLineRunner() {
//			
//			@Override
//			public void run(String... args) throws Exception {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		return runner;
		return args -> {
				Quote quote = restTemplate.getForObject(URL, Quote.class);
				log.info(quote.toString());
		};
	}
}
