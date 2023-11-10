package com.book.consultant.appointment.service.kafkaconfiguration;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * This is the Kafka Configuration Class
 * For configuration purpose we use set the boostrap server, key and value
 */

@Configuration
public class KafkaConfig {

    KafkaProducer<String, String> kafkaProducer;
    @Value("${kafka_url}")
    private String kafkaUrl;

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        kafkaProducer = new KafkaProducer<String, String>(properties);
        return kafkaProducer;
    }

    public void close(){
        kafkaProducer.close();
    }
}
