package com.payment.simplepaymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SimplePaymentGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplePaymentGatewayApplication.class, args);
	}

}
