# Find the dog server

This repository contains the code related to the server, handling the requests of image processing from the frontend, for the find-the-dog project. It is only meant for learning purposes.

## Description

The code handles the the requests from the frontend, by implementing an API that can receiving images from the post requests at frontend, and redirecting them to a queueing system through a kafka topic, that is later processed by an independent python microservice module that process the images. The processed images are then ontained by consuming from a second kafka topic and made available to the frontend through API request.

Main directory tree:

```
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── findTheDog
│   │   │           └── app
│   │   │               ├── App.java
│   │   │               ├── FindTheDogController.java
│   │   │               ├── ImageController.java
│   │   │               └── KafkaImageConsumer.java
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java
│           └── com
│               └── findTheDog
│                   └── app
│                       └── AppTest.java
```

## Understand the code

- FindTheDogController: Receives the image from the frontend, through API request, then sends the image as byte stream to the kafka topic, for it to be handles by the independent processing microservice.
- KafkaImageConsumer: Consumes the processed images produced by the independent processing microservice.
- ImageController: Sends the processed image to the frontend, through API request.
- application.yml: Contains the configuration for kafka producer and consumer.

## Run the code

Assuming maven, java and spring-boot are installed and running, as well as kafka and zookeeper (see additional documentation under _docs_), run:

```
mvn spring-boot:run
```
