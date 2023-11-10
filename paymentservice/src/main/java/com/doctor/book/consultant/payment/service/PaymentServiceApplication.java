package com.doctor.book.consultant.payment.service;

import com.doctor.book.consultant.payment.service.kafkaconfiguration.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class PaymentServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PaymentServiceApplication.class, args);

		KafkaConfig kafkaMessageProducer = context.getBean(KafkaConfig.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			kafkaMessageProducer.close();
			context.close();
		}));
	}

}
