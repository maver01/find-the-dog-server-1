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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
            // Convert the uploaded image to BufferedImage
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));

            // Convert the image to black and white
            BufferedImage blackAndWhiteImage = convertToBlackAndWhite(originalImage);

            // Convert the processed BufferedImage back to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(blackAndWhiteImage, "jpg", baos);
            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            // Prepare the response headers and return the image
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=processed_" + image.getOriginalFilename());
            headers.add(HttpHeaders.CONTENT_TYPE, image.getContentType());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Function to convert a BufferedImage to black and white
    private BufferedImage convertToBlackAndWhite(BufferedImage originalImage) {
        BufferedImage blackAndWhiteImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY); // Set to grayscale

        Graphics2D graphics = blackAndWhiteImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, null);
        graphics.dispose();

        return blackAndWhiteImage;
    }
}
