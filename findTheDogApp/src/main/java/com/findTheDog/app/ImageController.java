package com.findTheDog.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for this origin
public class ImageController {

    private final Logger logger = LoggerFactory.getLogger(KafkaImageProducer.class);
    // Use a map to store processed images with requestId as the key
    private final Map<String, byte[]> processedImages = new ConcurrentHashMap<>();

    // Endpoint to get the processed image by requestId
    @GetMapping("/processed-image/{requestId}")
    public ResponseEntity<ByteArrayResource> getProcessedImage(@PathVariable String requestId) {
        byte[] processedImage = processedImages.get(requestId);

        if (processedImage == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No image found for this requestId
        }

        // Return the processed image
        logger.info("Sending image with requestId {} to frontend.", requestId);
        ByteArrayResource resource = new ByteArrayResource(processedImage);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=processed_image_" + requestId + ".jpg");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // Method to update the processed image with requestId (called by the Kafka
    // consumer)
    public void updateProcessedImage(String requestId, byte[] imageBytes) {
        processedImages.put(requestId, imageBytes);
    }
}
