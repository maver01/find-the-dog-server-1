package com.findTheDog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.KafkaException;

import java.io.IOException;
import java.util.Base64;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Enable CORS for all origins
class FindTheDogController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // Inject the Kafka template to send messages
    public FindTheDogController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeImage(@RequestParam("image") MultipartFile image) {
        try {
            // Convert image to byte array
            byte[] imageBytes = image.getBytes();

            // Encode the byte array as Base64 string to send via Kafka
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

            // Send the encoded image to Kafka (to the Python microservice)
            try {
                kafkaTemplate.send("image-processing-topic", encodedImage);
                // Acknowledge that the image was sent
                return new ResponseEntity<>("Image sent to processing queue", HttpStatus.OK);
            } catch (KafkaException e) {
                // Handle Kafka exception when the node is not available
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
