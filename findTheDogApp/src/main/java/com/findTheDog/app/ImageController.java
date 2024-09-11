package com.findTheDog.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping; // Add this import
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for this origin
public class ImageController {

    private byte[] processedImage; // This will store the latest processed image

    // Endpoint to get the processed image
    @GetMapping("/processed-image")
    public ResponseEntity<ByteArrayResource> getProcessedImage() {
        if (processedImage == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Return the processed image
        ByteArrayResource resource = new ByteArrayResource(processedImage);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=processed_image.jpg");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // Method to update the processed image (called by the Kafka consumer)
    public void updateProcessedImage(byte[] imageBytes) {
        this.processedImage = imageBytes;
    }
}
