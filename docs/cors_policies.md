# CORS policies

From ChatGPT.

When your React frontend (running on one domain, e.g., localhost:3000) communicates with your Java Spring Boot backend (running on another domain, e.g., localhost:8080), CORS (Cross-Origin Resource Sharing) policies control whether the browser will allow the frontend to make requests to the backend.

Since your setup involves different ports (3000 for React and 8080 for Spring Boot), the browser sees them as separate domains. Without proper CORS handling, the browser may block these requests.

## Set ports

If you don't explicitly set the ports:

- React: By default, the development server will run on port 3000.
- Spring Boot: By default, the Spring Boot server runs on port 8080.

### React

In the package.json file, add:

```
"scripts": {
    "start": "PORT=3000 react-scripts start"
}
```

This manually sets the React app to run on port 3000.

### Spring Boot

In the application.yml file:

```
server:
    port: 8080
```

This manually sets Spring Boot to run on port 8080.
