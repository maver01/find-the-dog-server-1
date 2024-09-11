# Additional documentation: API requests

From ChatGPT. Exaples use spring-boot and react.

API requests typically handle communication between the frontend (e.g: React) and the backend (e.g.: Spring Boot). Here's a breakdown of common API request types and where they should be implemented:

## 1. GET Requests

- Purpose: Retrieve data from the server, like user details, product listings, or any resource the user needs to view.

- Backend: The Spring Boot backend should expose GET endpoints to handle these requests.

- Frontend: The React frontend will call these APIs (e.g., using fetch or Axios) to display data to the user.

- Testing: Assuming endpoint is `http://localhost:8080/api/processed-image`, the GET method can be tested from terminal by running:

  ```
  curl http://localhost:8080/api/processed-image
  ```

  Use `curl http://localhost:8080/api/processed-image -v | jq` to add more info to the request.

Example: `/api/v1/users/{id}` – to get user information by ID.

- Spring boot:

```
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // Simulate a user object for the example
        User user = new User(id, "John Doe", "john@example.com");
        return ResponseEntity.ok(user);
    }
}
...
```

- React:

```
import { useState, useEffect } from 'react';

const UserComponent = ({ userId }) => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await fetch(`/api/v1/users/${userId}`);
                const data = await response.json();
                setUser(data);
            } catch (error) {
                console.error('Error fetching user:', error);
            }
        };

        fetchUser();
    }, [userId]);
    ...
```

## 2. POST Requests

- Purpose: Send data to the server, typically for creating a new resource, such as registering a new user or uploading a file.
- Backend: Handle data validation and business logic. The backend processes the data and stores it in a database.
- Frontend: The React app would initiate the request when the user submits a form or performs some action.
- Testing: Assuming endpoint is `http://localhost:8080/api/v1/users`, the POST method can be tested from terminal by running:

  ```
  curl -i -X POST `http://localhost:8080/api/v1/users` -H "Content-Type: application/json" -d "the data to be sent"
  ```

Example: `/api/v1/users` – to create a new user.

- Spring boot:

```
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React frontend
public class UserController {

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // For simplicity, we'll assume the user is created and just return the same user object
        user.setId(1L); // Simulate setting a new ID
        return ResponseEntity.ok(user);
    }
}
...
```

- React:

```
import { useState } from 'react';

const CreateUserComponent = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [response, setResponse] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/v1/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name, email }), // Send the user data
            });
            const data = await response.json();
            setResponse(data);
        } catch (error) {
            console.error('Error creating user:', error);
        }
    };
    ...
```

## 3. PUT / PATCH Requests

- Purpose: Update an existing resource. Use PUT for full updates and PATCH for partial updates.
- Backend: The backend would accept and process the updated information, ensuring consistency with the database.
- Frontend: Triggered when the user updates data in a form or through a UI element (like editing a profile).
- Example: `/api/v1/users/{id}` – to update user details.

## 4. DELETE Requests

- Purpose: Remove a resource from the server, like deleting an account or removing a post.
- Backend: The backend will remove the resource from the database and handle any business logic associated with the deletion.
- Frontend: Called when the user performs a delete action (e.g., clicking a "delete" button).
- Example: `/api/v1/users/{id}` – to delete a user by ID.

## 5. Authentication (Optional)

- Purpose: Secure the API using authentication mechanisms like JWT tokens.
- Backend: Validate tokens or credentials (e.g., in POST `/api/v1/auth/login`), and protect other API endpoints.
- Frontend: Handle user login, storing the token securely (e.g., localStorage) and passing it in subsequent requests.
  Where to Define API Requests:
- Frontend (React): Initiate requests using fetch/Axios to interact with your Spring Boot API. You will mainly handle UI events that require API calls.
- Backend (Spring Boot): Define the actual API endpoints and handle business logic, validation, and data persistence.
- This flow ensures clear separation between frontend presentation and backend logic.
