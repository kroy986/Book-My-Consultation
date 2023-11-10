package com.doctor.book.consultant.doctor.server;

import com.doctor.book.consultant.doctor.server.kafkaconfiguration.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
@EnableCaching
public class DoctorServerApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DoctorServerApplication.class, args);

		KafkaConfig kafkaMessageProducer = context.getBean(KafkaConfig.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			kafkaMessageProducer.close();
			context.close();
		}));

	}

}
