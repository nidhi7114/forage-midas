# Midas
Project repo for the JPMC Advanced Software Engineering Forage program

This repository contains my completed solution for the **JPMorgan Chase – Advanced Software Engineering Virtual Experience** on Forage.

The project simulates a real-world financial transaction processing system built using **Spring Boot, Apache Kafka, and RESTful services**, focusing on backend system design, message-driven architecture, and service integration.

---

## What was done

### Event-Driven Transaction Processing
- Consumed and deserialized transaction messages using **Apache Kafka**
- Implemented validation logic for users and balances
- Persisted transactions and user data using **Spring Data JPA** and an **H2 database**

### Incentive API Integration
- Integrated an external **Incentive REST API** using `RestTemplate`
- Applied incentive amounts to recipient balances without affecting senders
- Ensured transactional consistency across database updates

### Balance Query REST API
- Exposed a `/balance` **GET** endpoint on port **33400**
- Returns user balances as JSON using a dedicated `Balance` model
- Gracefully handles non-existent users by returning a zero balance

### Testing & Verification
- Used **Embedded Kafka** for integration testing
- Verified correctness using Maven test suites
- Performed debugger-driven inspection for transaction flow validation

---

## Tech Stack

- Java  
- Spring Boot  
- Apache Kafka  
- Spring Data JPA  
- REST APIs  
- H2 Database  
- Maven  
- JUnit  

---

## About the Program

This project was completed as part of the **JPMorgan Chase Advanced Software Engineering Job Simulation** on Forage, designed to mirror real-world backend engineering tasks in financial systems.

🔗 Simulation link: https://www.theforage.com/simulations/jpmorgan/advanced-software-engineering-r0fm
