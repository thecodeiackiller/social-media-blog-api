# Social Media Blog API

## Project Overview
This project was developed as part of an 11-week Java training course. The goal was to build the backend of a hypothetical social media app, similar to a micro-blogging or messaging platform. The backend manages user accounts and the messages they post, providing endpoints for handling user registration, login, message creation, retrieval, deletion, and updates.

## Database Tables
The project utilizes two main database tables:

- **Account**: Manages user information.
  - `account_id` (Primary Key)
  - `username` (Unique)
  - `password`
  
- **Message**: Stores user messages.
  - `message_id` (Primary Key)
  - `posted_by` (Foreign Key referencing `Account.account_id`)
  - `message_text`
  - `time_posted_epoch`

## Implemented Features

### 1. User Registration
The `POST /register` endpoint was implemented to allow users to create new accounts. The system successfully checks that the username is not blank, the password is at least 4 characters long, and that the username is unique. Valid registrations return the created account details, including the `account_id`, with a 200 status code. Invalid attempts return a 400 status.

### 2. User Login
The `POST /login` endpoint verifies user credentials. Successful logins return the account information (including `account_id`) if the provided username and password match an existing account. Failed login attempts return a 401 Unauthorized status.

### 3. Message Creation
Users can create messages using the `POST /messages` endpoint. Messages are validated to ensure the `message_text` is not blank, does not exceed 255 characters, and that the `posted_by` field references a valid user. Successful message creation results in the message being saved to the database, returning the message data (including the `message_id`) with a 200 status.

### 4. Retrieve All Messages
The `GET /messages` endpoint retrieves all messages stored in the database. Even if no messages exist, the endpoint returns an empty list with a 200 status.

### 5. Retrieve a Message by ID
The `GET /messages/{message_id}` endpoint retrieves a single message by its ID. If the message exists, its data is returned. If not, an empty response with a 200 status is sent.

### 6. Delete a Message by ID
The `DELETE /messages/{message_id}` endpoint allows users to delete messages by ID. If the message existed, it is deleted from the database, and the deleted message is returned with a 200 status. If the message did not exist, an empty response is returned, maintaining idempotency.

### 7. Update a Message by ID
The `PATCH /messages/{message_id}` endpoint enables users to update a message's text. The update is successful if the message exists and the new text is valid (not blank and under 255 characters). The updated message is returned with a 200 status.

### 8. Retrieve Messages by User
The `GET /accounts/{account_id}/messages` endpoint retrieves all messages posted by a specific user. The endpoint returns a list of messages or an empty list if no messages exist, with a 200 status.

## Project Structure
This project follows a 3-layer architecture:
- **Controller Layer**: Contains endpoints for API interactions.
- **Service Layer**: Handles business logic, including validation and processing.
- **DAO Layer**: Interacts with the database for CRUD operations.

The provided `SocialMediaController` class was extended to handle the required API routes, while custom `DAO` and `Service` classes were designed to persist and manage data. The `Main.java` class allowed for manual testing and running the application. The project used the `ConnectionUtil` class to handle database connections, and the SQL script in `src/main/resources` was used to set up the database schema.

---

This project demonstrates the implementation of a basic social media backend, handling user and message management while adhering to RESTful API design principles.
