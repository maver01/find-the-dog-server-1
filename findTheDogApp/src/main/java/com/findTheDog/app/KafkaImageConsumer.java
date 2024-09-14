//docs: https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages.html

package com.findTheDog.app;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaImageConsumer {

    private final ImageController imageController;
    private final Logger logger = LoggerFactory.getLogger(KafkaImageConsumer.class);

    public KafkaImageConsumer(ImageController imageController) {
        this.imageController = imageController;
    }

    @KafkaListener(topics = "image-output-topic", groupId = "find-the-dog-group")
    public void consumeProcessedImage(@Header(KafkaHeaders.RECEIVED_KEY) String requestId, @Payload String message) {
        logger.info("Received processed image and request id from Kafka");

        // Decode the Base64-encoded image
        byte[] imageBytes = Base64.getDecoder().decode(message);
        logger.info("Image with requestId {} received from processing queue", requestId);

        // Update the processed image in ImageController
        imageController.updateProcessedImage(requestId, imageBytes);

        logger.info("Processed image updated in ImageController");
    }
}