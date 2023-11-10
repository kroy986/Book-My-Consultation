package com.book.consultation.user.service;

import com.book.consultation.user.service.kafkaconfiguration.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(UserserviceApplication.class, args);

		KafkaConfig kafkaMessageProducer = context.getBean(KafkaConfig.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			kafkaMessageProducer.close();
			context.close();
		}));
	}

}
