package com.findTheDog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka // Enables Kafka Listener
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
