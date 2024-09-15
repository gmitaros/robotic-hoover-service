# Robotic Hoover Service
[![Java CI with Maven](https://github.com/gmitaros/robotic-hoover-service/actions/workflows/build.yml/badge.svg)](https://github.com/gmitaros/robotic-hoover-service/actions/workflows/build.yml)

## Overview

This service simulates an imaginary robotic hoover navigating a room and cleaning dirt patches based on input instructions. The hoover moves within a defined grid room, cleans patches of dirt it encounters, and reports its final position and the number of patches cleaned.
## Technologies Used

- **Java 17**: For implementing the application logic.
- **Spring Boot**: To build the RESTful API service.
- **Maven**: For project management and building.
- **JUnit 5**: For writing and running tests.
- **SLF4J with Logback**: For logging.
- **Lombok**: To reduce boilerplate code.

## How to Build and Run

### Prerequisites

- **Java 17+**: Ensure Java is installed and JAVA_HOME is set.
- **Maven 3+**: For building the project.
- **Git**: To clone the repository.

### How to Obtain the Service
Clone the public GitHub repository:

```bash
git clone https://github.com/gmitaros/robotic-hoover-service.git
cd robotic-hoover-service
```

### Build the Project
Navigate to the project directory and run:

```bash
mvn clean install
```

### Run the Service

Start the application using the Spring Boot plugin:

```bash
java -jar target/robotichoover-0.0.1-SNAPSHOT.jar
```

The service will start and listen on http://localhost:8080.

## API Usage
### Input Payload
The service expects a JSON payload with the following structure:

- **roomSize**: An array [X, Y] defining the dimensions of the room.
- **coords**: An array [X, Y] specifying the hoover's initial position.
- **patches**: An array of [X, Y] positions where patches of dirt are located.
- **instructions**: A string of characters representing movement instructions.

Example:
```bash
{
  "roomSize": [5, 5],
  "coords": [1, 2],
  "patches": [
    [1, 0],
    [2, 2],
    [2, 3]
  ],
  "instructions": "NNESEESWNWW"
}
```

### Output Payload
The service returns a JSON payload with:

- **coords**: The final coordinates of the hoover after executing the instructions.
- **patches**: The number of patches of dirt the hoover cleaned.
  Example:
```bash
{
  "coords": [1, 3],
  "patches": 1
}
```
## Example Request and Response

### Sending a Request
Use curl or any API testing tool (e.g., Postman):
```bash
curl -L 'http://localhost:8080/clean' -H 'Content-Type: application/json' -d '{
    "roomSize": [
        5,
        5
    ],
    "coords": [
        1,
        2
    ],
    "patches": [
        [
            1,
            0
        ],
        [
            2,
            2
        ],
        [
            2,
            3
        ]
    ],
    "instructions": "NNESEESWNWW"
}'
```
### Expected Response
```bash
{
  "coords": [1, 3],
  "patches": 1
}
```
## Testing the Service
### Running Unit Tests
Unit tests are located in `src/test/java`. To run unit tests:
```bash
mvn test
```
### Running Integration Tests
Integration tests are included to verify the application's behavior as a whole.

To run integration tests:
```bash
mvn verify
```
### Test Coverage
You can generate a test coverage report using JaCoCo:
```bash
mvn clean verify jacoco:report
```
The report will be generated at target/site/jacoco/index.html.

## Continuous Integration

This project uses GitHub Actions for continuous integration. The build status is indicated by the badge above. Every push and pull request triggers the workflow, which builds the project and runs all tests.


### Instruction Handling

- **Valid Instructions:** `N` (North), `S` (South), `E` (East), `W` (West)
- **Invalid Instructions:** Any character not among the valid instructions is ignored.
- **Logging:** Invalid instructions are logged as warnings on the server side.
- **Response:** The service does not indicate to the client which instructions were invalid unless specified.


## Logging

The application uses SLF4J with Logback for logging. Logging configurations are defined in `src/main/resources/application.properties`.

### Adjusting Log Levels

By default, the root logging level is set to `INFO`, and the application package `com.gmitaros.robotichoover` is set to `DEBUG` for detailed logs.

To adjust the logging levels, modify `application.properties`:

```properties
# Set the root logging level (ERROR, WARN, INFO, DEBUG, TRACE)
logging.level.root=INFO

# Set the logging level for the application package
logging.level.com.gmitaros.robotichoover=DEBUG
```
## Approach and Assumptions
### API and System Design
- **RESTful API**: Implemented using Spring Boot to provide a simple and scalable RESTful interface.
- **JSON Payloads**: Uses JSON for input and output for ease of integration and testing.
### Assumptions
- **Grid Coordinates**: The room is represented as a grid with the origin `(0, 0)` at the bottom-left corner.
- **Room Boundaries**: The hoover cannot move beyond the defined room dimensions.
- **Hoover Behavior**:
  - Always on and starts cleaning immediately.
  - Cleans a patch when it moves over it.
  - Can clean a patch only once.
- **Instruction Processing**: Invalid instructions are ignored, and the hoover continues processing the rest.
### Dealing with Uncertainty
- **Input Validation**: Basic validation is performed to ensure essential fields are present.
- **Error Handling**: The service responds with appropriate HTTP status codes and messages for invalid inputs.

## Code Structure and Organization
- **Controller Layer**: Handles HTTP requests and responses.
- **Service Layer**: Contains the business logic for processing the hoover's movement and cleaning.
- **Model Classes**: Define the data structures for input and output payloads.
- **Exception Handling**: Custom exceptions and handlers are used for better error management.
- **Logging**: Integrated at various levels to trace the computation and aid in debugging.

## Project Structure
```bash
src/
├── main/
│   ├── java/com/gmitaros/robotichoover/
│   │   ├── controller/
│   │   │   └── CleaningController.java
│   │   ├── model/
│   │   │   ├── InputPayload.java
│   │   │   ├── OutputPayload.java
│   │   │   └── Position.java
│   │   ├── service/
│   │   │   └── CleaningService.java
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/gmitaros/robotichoover/
    │   ├── controller/
    │   │   └── CleaningControllerIntegrationTest.java
    │   └── service/
    │       └── CleaningServiceTest.java
    └── resources/
```



