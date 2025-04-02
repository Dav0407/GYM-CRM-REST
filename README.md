# Trainee & Trainer Controller - API Documentation

## Overview
This document provides RESTful API endpoints for managing both trainee and trainer profiles, including registration, retrieval, updating, deletion, status switching, and training sessions. Each endpoint requires authentication via HTTP headers.

## Base URLs
```
/api/v1/trainees
/api/v1/trainers
/api/v1/trainings
```

---

# Trainee Endpoints

## 1. Register a Trainee
#### Endpoint
```
POST /api/v1/trainees/register
```
#### Request Body (JSON)
```json
{
  "firstName": "string",
  "lastName": "string",
  "dateOfBirth": "YYYY-MM-DD",
  "address": "string"
}
```
#### Validation Constraints
- `firstName`: **Required**, cannot be blank.
- `lastName`: **Required**, cannot be blank.
- `dateOfBirth`: Optional.
- `address`: Optional.

#### Response
- **201 Created**: Returns the created trainee profile.
- **400 Bad Request**: If request validation fails.

---

## 2. Get Trainee Profile
#### Endpoint
```
GET /api/v1/trainees/{username}
```
#### Request Headers
```text
Username: <admin_username>
Password: <admin_password>
```
#### Path Parameters
- `username` (String) - The username of the trainee.

#### Response
- **302 Found**: Returns the trainee's profile.
- **404 Not Found**: If the trainee does not exist.

---

## 3. Update Trainee Profile
#### Endpoint
```
PUT /api/v1/trainees
```
#### Request Headers
```text
Username: <admin_username>
Password: <admin_password>
```
#### Request Body (JSON)
```json
{
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "dateOfBirth": "YYYY-MM-DD",
  "address": "string",
  "isActive": true
}
```
#### Validation Constraints
- `firstName`: **Required**, cannot be null or empty.
- `lastName`: **Required**, cannot be null or empty.
- `username`: **Required**, cannot be null or empty.
- `dateOfBirth`: Optional.
- `address`: Optional.
- `isActive`: **Required**, cannot be null.

#### Response
- **202 Accepted**: Returns the updated trainee profile.
- **400 Bad Request**: If request validation fails.

---

## 4. Delete Trainee Profile
#### Endpoint
```
DELETE /api/v1/trainees/{username}
```
#### Request Headers
```text
Username: <admin_username>
Password: <admin_password>
```
#### Path Parameters
- `username` (String) - The username of the trainee to delete.

#### Response
- **202 Accepted**: Returns the deleted trainee profile.
- **404 Not Found**: If the trainee does not exist.

---

## 5. Switch Trainee Status
#### Endpoint
```
PATCH /api/v1/trainees/{trainee-username}/status
```
#### Request Headers
```text
Username: <admin_username>
Password: <admin_password>
```
#### Path Parameters
- `trainee-username` (String) - The username of the trainee whose status will be updated.

#### Response
- **202 Accepted**: Returns the updated trainee profile.
- **404 Not Found**: If the trainee does not exist.

---

# Trainer Endpoints

## 1. Register a Trainer
#### Endpoint
```
POST /api/v1/trainers/register
```
#### Request Body (JSON)
```json
{
  "firstName": "string",
  "lastName": "string",
  "trainingType": "string"
}
```
#### Validation Constraints
- `firstName`: **Required**, cannot be blank.
- `lastName`: **Required**, cannot be blank.
- `trainingType`: **Required**, cannot be blank.

#### Response
- **201 Created**: Returns the created trainer profile.
- **400 Bad Request**: If request validation fails.

---

## 2. Get Trainer Profile
#### Endpoint
```
GET /api/v1/trainers/{username}
```
#### Request Headers
```text
Username: <admin_username>
Password: <admin_password>
```
#### Path Parameters
- `username` (String) - The username of the trainer.

#### Response
- **302 Found**: Returns the trainer's profile.
- **404 Not Found**: If the trainer does not exist.

---

# Training Endpoints

## 1. Add Training
#### Endpoint
```
POST /api/v1/trainings
```
#### Request Body (JSON)
```json
{
  "traineeUsername": "string",
  "trainerUsername": "string",
  "trainingName": "string",
  "trainingDate": "YYYY-MM-DD",
  "trainingDuration": 60
}
```
#### Response
- **201 Created**: Returns the created training.

---

## 2. Get Trainings for Trainee
#### Endpoint
```
GET /api/v1/trainings/trainees
```
#### Request Body (JSON)
```json
{
  "traineeUsername": "string",
  "from": "YYYY-MM-DD",
  "to": "YYYY-MM-DD",
  "trainerUsername": "string",
  "trainingType": "string"
}
```
#### Response
- **302 Found**: Returns the list of trainings for the trainee.

---

## 3. Get Trainings for Trainer
#### Endpoint
```
GET /api/v1/trainings/trainers
```
#### Request Body (JSON)
```json
{
  "trainerUsername": "string",
  "from": "YYYY-MM-DD",
  "to": "YYYY-MM-DD",
  "traineeUsername": "string"
}
```
#### Response
- **302 Found**: Returns the list of trainings for the trainer.

---

## 4. Get Training Types
#### Endpoint
```
GET /api/v1/trainings/types
```
#### Response
- **302 Found**: Returns the list of available training types.

---

## Authentication
All endpoints (except registration) require authentication via HTTP headers:
- `Username`: The admin username
- `Password`: The admin password

## Error Handling
- `400 Bad Request`: Invalid input data
- `401 Unauthorized`: Incorrect credentials
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server failure

## Technologies Used
- Java 17+
- Spring Boot
- Spring Validation
- JPA/Hibernate

## Author
Developed by Davron.

## License
MIT License

