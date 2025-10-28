# 🏦 Banking API using Spring Boot, Spring Security, and JWT

A simple yet secure banking REST API built with Spring Boot, featuring JWT authentication, role-based access control, and transaction management. This project demonstrates real-world backend development principles like layered architecture, secure authentication, and database integration.

---

## 🧩 Tech Stack

Java 17
Spring Boot 3
Spring Security + JWT
Spring Data JPA (Hibernate)
PostgreSQL
Maven
Lombok

---

## ⚙️ How to Run

```bash
# 1. Clone the repository
git clone https://github.com/zaidnshr1/BankingApp.git
cd BankingApp

# 2. Configure database & JWT Key in application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb (also create a database in your postgres)
spring.datasource.username=youruser
spring.datasource.password=yourpass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
application.security.jwt.secret-key=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
application.security.jwt.expiration=86400000

# 3. Build the project
mvn clean install

# 4. Run the application
mvn spring-boot:run
```

Server runs at:
👉 [http://localhost:8080](http://localhost:8080)

---

## 🔑 Authentication

All private endpoints require JWT token.
Register to get your Account and Profile in headers: `/api/v1/banking/register`
Login to get your token via `/api/v1/auth/login`, then include it in `Authorization: Bearer <your_jwt_token>`.

---

## 🚀 Main Endpoints

### 🔓 Public

POST  `/api/v1/banking/register`  — Register new account
POST  `/api/v1/auth/login`  — Authenticate and receive JWT token

### 🔒 Private (Requires JWT)

GET   `/api/v1/banking/{accountNumber}`  — Get account details
POST  `/api/v1/banking/deposit`  — Deposit money
POST  `/api/v1/banking/transfer`  — Transfer between accounts
GET   `/api/v1/banking/history/{accountNumber}`  — View transaction history

---

## 🧑‍💻 Author

Zaid Anshori
Backend Developer (Java | Spring Boot)
