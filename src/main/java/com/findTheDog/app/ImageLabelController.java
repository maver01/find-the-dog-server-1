package com.findTheDog.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "localhost:3000")
public class ImageLabelController {

    private final Logger logger = LoggerFactory.getLogger(ImageLabelController.class);
    // Use a map to store processed class labels with requestId as the key
    private final Map<String, String> processedLabels = new ConcurrentHashMap<>();

    // Endpoint to get the processed label by requestId
    @GetMapping("/processed-label/{requestId}")
    public ResponseEntity<String> getProcessedLabel(@PathVariable String requestId) {
        String classLabel = processedLabels.get(requestId);

        if (classLabel == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No label found for this requestId
        }

        // Return the processed class label
        logger.info("Sending class label with requestId {} to frontend: {}", requestId, classLabel);
        return new ResponseEntity<>(classLabel, HttpStatus.OK);
    }

    // Method to update the processed label with requestId (called by the Kafka
    // consumer)
    public void updateProcessedLabel(String requestId, String classLabel) {
        processedLabels.put(requestId, classLabel);
    }
}
