package com.findTheDog.app;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.KafkaException;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Properties;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Enable CORS for all origins
public class KafkaImageProducer {

    private final Logger logger = LoggerFactory.getLogger(KafkaImageProducer.class);
    private final Producer<String, String> producer;

    // Constructor to initialize Kafka producer
    public KafkaImageProducer() {
        // Setup Kafka producer properties
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("transactional.id", "image-transactional-id");

        // Create Kafka producer
        this.producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());
        // Initialize transactions
        this.producer.initTransactions();
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeImage(@RequestParam("image") MultipartFile image) {
        try {
            // Convert the image to a byte array
            byte[] imageBytes = image.getBytes();
            // Encode the image as Base64 string
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

            // Start a Kafka transaction
            try {
                producer.beginTransaction();
                // Send the encoded image to Kafka topic
                producer.send(new ProducerRecord<>("image-processing-topic", encodedImage));
                // Commit the transaction
                producer.commitTransaction();
                logger.info("Image sent to processing queue");
                return new ResponseEntity<>("Image sent to processing queue", HttpStatus.OK);
            } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
                // Close the producer on unrecoverable exceptions
                producer.close();
                logger.error("Producer cannot recover, closing.", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send image to processing queue.");
            } catch (KafkaException e) {
                // Abort transaction on any other Kafka exceptions
                producer.abortTransaction();
                logger.error("Transaction aborted, retry.", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Transaction aborted, retry sending the image.");
            }
        } catch (IOException e) {
            logger.error("Failed to read image file.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to read image file.");
        }
    }
}
