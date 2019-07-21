package com.hsbc.boardie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class BoardieApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardieApplication.class, args);
	}

}
