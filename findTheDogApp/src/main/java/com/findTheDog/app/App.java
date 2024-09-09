package com.findTheDog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

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

    @PostMapping("/analyze")
    public ResponseEntity<Resource> analyzeImage(@RequestParam("image") MultipartFile image) {
        try {
            // You can process the image here, for now we just return the same image

            // Convert MultipartFile to ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(image.getBytes());

            // Prepare the response headers and return the image
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getOriginalFilename());
            headers.add(HttpHeaders.CONTENT_TYPE, image.getContentType());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
