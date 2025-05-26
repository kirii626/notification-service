# Notification Service

A Spring Boot microservice for handling notifications, including PDF generation and email delivery, with RabbitMQ for messaging and Eureka for service discovery.

---

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
  - [Running Tests](#running-tests)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Environment Variables](#environment-variables)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **PDF Generation:** Creates accreditation PDFs using iText.
- **Email Notifications:** Sends emails with PDF attachments.
- **RabbitMQ Integration:** Asynchronous event-driven communication.
- **Service Discovery:** Registers with Eureka for microservice architecture.
- **Test Coverage:** Unit and integration tests with JUnit 5 and Mockito.

---

## Architecture

[Client] --> [RabbitMQ] --> [Notification Service] --> [Email Service] | v [Eureka]

- **RabbitMQ:** Handles message queuing for accreditation events.
- **Notification Service:** Listens for events, generates PDFs, sends emails.
- **Eureka:** Enables service registration and discovery.

---

## Technologies

- Java 17+
- Spring Boot
- Maven
- RabbitMQ
- Eureka (Netflix OSS)
- iText (PDF generation)
- JUnit 5 & Mockito (testing)

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- RabbitMQ (running on `localhost:5672`)
- Eureka Server (running on `localhost:8761`)
- Gmail account for SMTP (update credentials in `application-test.properties`)

### Configuration

All configuration is managed via `src/main/resources/application.properties` and the different kind of types you need, such as dev mode, docker or test. 

**Sensitive data:**  
Do not commit real credentials. Use environment variables or a secrets manager for production.

Example properties:
```ini
server.port=8086
spring.rabbitmq.host=localhost
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
