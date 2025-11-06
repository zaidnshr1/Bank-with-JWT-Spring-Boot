# 🏦 Banking API using Spring Boot, Spring Security, and JWT

A simple yet secure banking REST API built with Spring Boot, featuring JWT authentication, role-based access control, and transaction management. This project demonstrates real-world backend development principles like layered architecture, secure authentication, and database integration.

---

## 🧩 Tech Stack

- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- Lombok
- JUnit & Mockito
- Springdoc-OpenAPI (Swagger)

---

## Project Overview
transaction/  
├─ pom.xml  
├─ README.md  
├─ src/  
│ ├─ main/  
│ │ ├─ java/  
│ │ │   └─ com/  
│ │ │       └─ zaid/  
│ │ │           └─ transaction/  
│ │ │               ├─ controller/  
│ │ │               │  ├─ AccountController.java  
│ │ │               │  ├─ AdministrationController.java  
│ │ │               │  └─ AuthController.java  
│ │ │               ├─ dto/  
│ │ │               │  ├─ AboutAccount.java  
│ │ │               │  ├─ AdminRegistrationRequest.java  
│ │ │               │  ├─ AdminRegistrationResponse.java  
│ │ │               │  ├─ AuthRequest.java  
│ │ │               │  ├─ AuthResponse.java  
│ │ │               │  ├─ ClientRegistrationRequest.java  
│ │ │               │  ├─ ClientRegistrationResponse.java  
│ │ │               │  ├─ DepositMoneyRequest.java  
│ │ │               │  ├─ DisableAccountRequest.java  
│ │ │               │  ├─ DisableAccountResponse.java  
│ │ │               │  ├─ TransactionHistory.java  
│ │ │               │  ├─ TransferRequest.java  
│ │ │               │  ├─ TransferResponse.java  
│ │ │               │  ├─ UpdateProfileRequest.java  
│ │ │               │  ├─ UpdateProfileResponse.java  
│ │ │               ├─ exception/  
│ │ │               │  ├─ AccountAlreadyExistException.java  
│ │ │               │  ├─ AccountNotFoundException.java  
│ │ │               │  ├─ EmailNotFoundException.java  
│ │ │               │  ├─ InvalidInputException.java  
│ │ │               │  ├─ InvalidPinException.java  
│ │ │               │  ├─ InvalidTransactionException.java  
│ │ │               │  ├─ RoleNotFoundException.java  
│ │ │               │  ├─ UnauthorizedAccessException.java  
│ │ │               │  ├─ UsernameNotFoundException.java  
│ │ │               │  └─ GlobalExceptionHandler.java  
│ │ │               ├─ model/  
│ │ │               │  ├─ Account.java  
│ │ │               │  ├─ Profile.java  
│ │ │               │  └─ Transaction.java  
│ │ │               ├─ repository/  
│ │ │               │  ├─ AccountRepository.java  
│ │ │               │  ├─ ProfileRepository.java  
│ │ │               │  └─ TransactionRepository.java  
│ │ │               ├─ security/  
│ │ │               │  ├─ config/  
│ │ │               │  │  ├─ SecurityBeans.java  
│ │ │               │  │  └─ SecurityConfig.java  
│ │ │               │  ├─  entity/  
│ │ │               │  │  ├─ Role.java  
│ │ │               │  │  └─ User.java  
│ │ │               │  ├─  repository/  
│ │ │               │  │  ├─ RoleRepository.java  
│ │ │               │  │  └─ UserRepository.java  
│ │ │               │  └─  service/  
│ │ │               │  ├─ CustomUserDetailsService.java  
│ │ │               │  ├─ JwtAuthFilter.java  
│ │ │               │  └─ JwtService.java  
│ │ │               └─ service/  
│ │ │                  ├─ AccountService.java  
│ │ │                  ├─ TransactionService.java  
│ │ │                  ├─ TransferService.java  
│ │ │                  └─ UserService.java  
│ │ └─ resources/  
│ │     ├─ application.properties  
│ │     └─ static/ (optional)  
│ └─ test/  
│    └─ java/  
│        └─ com/  
│           └─ zaid/  
│              └─ transaction/  
│                  └─ service/  
│                     ├─ TransferServiceIntegrationTest.java  
│                     ├─ TransferServiceUnitTest.java  
│                     └─ UserServiceTest.java  
└─ .gitignore

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

# 5. Go to the PostgreSql
insert into roles (id, name) values (1, ROLE_CLIENT), (2, ROLE_ADMIN);
```

Server runs at:
👉 [http://localhost:8080](http://localhost:8080)


## 🚀 Main Endpoints

### 🔓 Public

POST  `/api/v1/auth/register-admin` Register Admin account  
POST  `/api/v1/auth/login`  Authenticate and receive JWT token  

### 🔒 Private (Requires JWT)

## Admin (Jwt From Login Admin)
POST   `/api/v1/admin/register-user}`  Admin Register Client account  
PUT   `/api/v1/admin/disable-account`  Admin Disable Client Account  

## Client (Jwt From Login Client)
POST  `//api/v1/account/deposit`  Client can deposit money  
POST  `/api/v1/account/transfer`  Transfer to another accounts  
GET   `/api/v1/account/history/{accountNumber}`  View transaction history  
GET   `/api/v1/account/{accountNumber}`  View Account details  
PUT   `/api/v1/account/edit-profile`  Update first or last name  
