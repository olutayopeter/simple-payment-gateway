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



### 2. Webhook Notifications(Set up Kafka)

 - Install and start Apache Kafka. [Kafka Quickstart Guide](https://kafka.apache.org/quickstart)
Configure Gmail for Email Notifications:

  - The webhook notification service processes these updates and can be used by external systems to stay synchronized with the latest transaction information.

 - Update `application.properties` with your Gmail credentials:

        ```properties
        spring.mail.host=smtp.gmail.com
        spring.mail.port=587
        spring.mail.username=your-gmail-username@gmail.com
        spring.mail.password=your-gmail-password
        spring.mail.properties.mail.smtp.auth=true
        spring.mail.properties.mail.smtp.starttls.enable=true
        spring.mail.from=your-sender-email@gmail.com

   
###  **Receive Email Notifications**:
Upon a successful transaction, an email notification will be sent to the specified destination email address.

### Getting Started

git clone https://github.com/olutayopeter/simple-payment-gateway.git

### Build and run the application:

cd payment-gateway-service

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
