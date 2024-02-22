# Simple Payment Gateway 


## This Spring Boot application simulates a simple payment gateway middleware, facilitating transactions between a mock retail application and bank service
### Table of Contents
```
- [Overview](#overview)
- [Endpoints](#endpoints)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Configuration](#configuration)
- [Testing](#testing)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)
```

### Overview

This payment gateway service provides endpoints for initiating transactions, checking transaction status, and receiving webhook notifications for transaction updates. It uses an in-memory H2 database to simulate transaction records and banking service responses.

### Required software

The following are the initially required software pieces:

1. **Java 11**: JDK 11 LTS can be downloaded and installed from https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html

1. **Git**: it can be downloaded and installed from https://git-scm.com/downloads

1. **Maven**: Apache Maven is a software project management and comprehension tool, it can be downloaded from here https://maven.apache.org/download.cgi

1. **curl**: this command-line tool for testing HTTP-based APIs can be downloaded and installed from https://curl.haxx.se/download.html

1. **jq**: This command-line JSON processor can be downloaded and installed from https://stedolan.github.io/jq/download/

1. **Spring Boot Initializer**: This *Initializer* generates *spring* boot project with just what you need to start quickly! Start from here https://start.spring.io/


Follow the installation guide for each software website link and check your software versions from the command line to verify that they are all installed correctly.

## Endpoints

###. Initiate Transaction and check transaction status

- **Method:** POST
- **Endpoint:** `/api/v1/payment/initiate`
- **Request Example:**
  ```json
  {
    "amount": 1000,
    "transactionRef": "1234567834",
    "transactionDescription": "Payment for Electricity",
    "channel": "WEB",
    "currency": "NGN",
    "destinationAccount": "0692887918",
    "destinationAccountName": "Olutayo Adelodun",
    "destinationEmail": "olutayopeter2014@gmail.com",
    "destinationBankCode": "044",
    "sourceAccount": "0036251435",
    "sourceAccountName": "Edith Otulugbu",
    "sourceBankCode": "017",
    "sourceEmail": "olutayo.adelodun@9psb.com.ng"
}

# Response Example:

{
    "status": "00",
    "message": "The process was completed successfully",
    "data": {
        "amount": 1000.0,
        "transactionRef": "1234567834",
        "transactionDescription": "Payment for Electricity",
        "authorizationId": "977fa033bddc40999f51b5a34fc66ecb",
        "transactionDate": "2024-02-22T08:51:02.944217800",
        "paymentDate": "2024-02-22T08:51:02.944217800",
        "destinationEmail": "olutayopeter2014@gmail.com",
        "sourceEmail": "olutayo.adelodun@9psb.com.ng"
    }
}

 2. Check Transaction
 
 - **Method:** GET
- **Endpoint:** `/api/v1/payment/status/{transactionRef}`
- **Request Example:**
  ```json
  http://localhost:8080/api/v1/payment/status/1234567834

 Response Example:

{
    "status": "00",
    "message": "Account [0036251435] Debited successfully. Credit in progress.",
    "data": {
        "authorizationId": "977fa033bddc40999f51b5a34fc66ecb",
        "transactionRef": "1234567834",
        "amount": 1000.0,
        "paymentStatus": "SUCCESS",
        "transactionDescription": "Payment for Electricity",
        "transactionDate": "2024-02-22T08:51:02.944218",
        "paymentDate": "2024-02-22T08:51:02.944218",
        "currency": "NGN",
        "destinationAccount": "0692887918",
        "destinationBankCode": "044",
        "sourceAccount": "0036251435",
        "sourceBankCode": "017",
        "destinationEmail": "olutayopeter2014@gmail.com",
        "sourceEmail": "olutayo.adelodun@9psb.com.ng"
    }
}



### 2. Webhook Notifications

- **Endpoint:** `/webhook/notifications`
- **Details:** This endpoint has been implemented to receive webhook notifications for transaction updates. Whenever the status of a transaction changes, the payment gateway will send a POST request to this endpoint with details about the updated transaction.

  - The payload of the webhook notification includes information such as:
    - Transaction reference
    - Updated payment status
    - Authorization ID
    - Transaction description
    - Transaction date
    - Payment date
    - Currency
    - Destination account details
    - Source account details

  - The webhook notification service processes these updates and can be used by external systems to stay synchronized with the latest transaction information.

  - Ensure that your application has the necessary logic to securely handle and process incoming webhook notifications. Implement appropriate error handling and retry mechanisms to handle potential failures.

  - Developers integrating with this service should subscribe to the `/webhook/notifications` endpoint to receive real-time updates on transaction status changes.

  - **Sample Webhook Notification Payload:**
    ```json
    {
        "transactionRef": "12345678900000345",
        "paymentStatus": "SUCCESS",
        "authorizationId": "300008259334",
        "transactionDescription": "Payment for services",
        "transactionDate": "2022-02-21T12:34:56.789",
        "paymentDate": "2022-02-21T12:34:56.789",
        "currency": "NGN",
        "destinationAccount": "4589999044",
        "destinationBankCode": "044",
        "sourceAccount": "8909090989",
        "sourceBankCode": "058",
        "destinationEmail": "olutayopeter2014@gmail.com",
        "sourceEmail": "olutayo.adelodun@9psb.com.ng"
    }
    ```

  - Developers can use this information to update their systems or trigger specific actions based on the latest status of the payment transactions.

### Getting Started

git clone https://github.com/olutayopeter/simple-payment-gateway.git
cd payment-gateway-service

### Build and run the application:

./mvnw spring-boot:run

The service will be available at http://localhost:8080
```

### Usage:

-Make requests to the provided endpoints using a tool like Postman.
-Configure basic authentication with the username and password specified in your application.


### Configuration:

-The application uses an H2 in-memory database for simulation.
-Basic authentication is configured for securing the endpoints.


### Testing:

-Use JUnit tests to verify the functionality of the service.
-Postman can be used for manual testing with sample requests provided in the documentation.

### Documentation:

-API documentation is generated using Swagger and is available at http://localhost:8080/swagger-ui.html.