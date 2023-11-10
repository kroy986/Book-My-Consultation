package com.book.consultant.appointment.service;

import com.book.consultant.appointment.service.kafkaconfiguration.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class AppointmentserviceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AppointmentserviceApplication.class, args);

		KafkaConfig kafkaMessageProducer = context.getBean(KafkaConfig.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			kafkaMessageProducer.close();
			context.close();
		}));
	}

}
