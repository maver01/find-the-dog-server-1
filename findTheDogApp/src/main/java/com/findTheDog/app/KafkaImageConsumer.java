//docs: https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages.html

package com.findTheDog.app;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.logging.Logger;

@Service
public class KafkaImageConsumer {

    private final ImageController imageController;
    private static final Logger logger = Logger.getLogger(KafkaImageConsumer.class.getName());

    public KafkaImageConsumer(ImageController imageController) {
        this.imageController = imageController;
    }

    @KafkaListener(topics = "image-output-topic", groupId = "find-the-dog-group")
    public void consumeProcessedImage(String message) {
        logger.info("Received processed image from Kafka");

        // Decode the Base64-encoded image
        byte[] imageBytes = Base64.getDecoder().decode(message);

        // Update the processed image in ImageController
        imageController.updateProcessedImage(imageBytes);

        logger.info("Processed image updated in ImageController");
    }
}