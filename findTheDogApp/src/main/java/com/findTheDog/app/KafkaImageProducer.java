package com.findTheDog.app;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.findTheDog.app.telemetry.MetricsService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://172.18.0.4:3000") // Enable CORS for all origins
public class KafkaImageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(KafkaImageProducer.class);
    private final MetricsService metricsService;

    public KafkaImageProducer(KafkaTemplate<String, String> kafkaTemplate, MetricsService metricsService) {
        this.kafkaTemplate = kafkaTemplate;
        this.metricsService = metricsService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeImage(@RequestParam("image") MultipartFile image,
            @RequestParam("requestId") String requestId) {

        try { // make the span current
              // increment the gauge for images being processed
            metricsService.incrementImagesInProcess();

            String encodedImage = encodeImage(image);
            sendImageToKafka(requestId, encodedImage);
            // increment the counter for images sent to producer in Prometheus
            metricsService.incrementImagesSentToProducer();
            return ResponseEntity.ok("Image sent to processing queue");
        } catch (IOException e) {
            logger.error("Failed to read image file.", e);
            // send error to Prometheus
            metricsService.incrementErrorProducerCounter();
            // record exception in the span
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to read image file.");
        } catch (KafkaException e) {
            logger.error("Failed to send image to processing queue.", e);
            // send error to Prometheus
            metricsService.incrementErrorProducerCounter();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send image to processing queue.");
        } finally {
            // decrement the gauge for images being processed
            metricsService.decrementImagesInProcess();
        }
    }

    private String encodeImage(MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private void sendImageToKafka(String requestId, String encodedImage) {
        kafkaTemplate.send("image-processing-topic", requestId, encodedImage);
        logger.info("Image with requestId {} sent to processing queue", requestId);
    }
}
