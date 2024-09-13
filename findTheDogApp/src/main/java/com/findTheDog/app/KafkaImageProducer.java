//docs: https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html

package com.findTheDog.app;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Enable CORS for all origins
public class KafkaImageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(KafkaImageProducer.class);

    // Inject the Kafka template to send messages
    public KafkaImageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeImage(@RequestParam("image") MultipartFile image) {
        try {
            // Convert image to byte array
            byte[] imageBytes = image.getBytes();

            // Encode the byte array as a Base64 string to send via Kafka
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

            // Send the encoded image to Kafka (to the Python microservice)
            try {
                kafkaTemplate.send("image-processing-topic", encodedImage);
                // Acknowledge that the image was sent
                logger.info("Image sent to processing queue");
                return new ResponseEntity<>("Image sent to processing queue", HttpStatus.OK);
            } catch (KafkaException e) {
                // Handle Kafka exception when the node is not available
                logger.error("Failed to send image to processing queue.", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send image to processing queue.");
            }
        } catch (IOException e) {
            logger.error("Failed to read image file.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to read image file.");
        }
    }
}