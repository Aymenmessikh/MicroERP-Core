# MicroERP-Core

**MicroERP-Core** is a modular ERP platform built on a **microservices architecture**, serving as the foundation for a modern, extensible, and resilient ERP system.  
It addresses key non-functional requirements such as **security**, **auditability**, **observability**, **centralized notification management**, and **advanced authentication**.
![MicroERP-Core](/MicroERP-Core.png)

---

## 🔐 1. Authentication Service – Centralized Identity Provider

### 🛠️ Technologies Used
- **Keycloak**, **Spring Security**, **OAuth2/OpenID**
- **Express.js**, **Solidity**, **Ganache**, **MetaMask**, **Web3.js**

The **Authentication Service** ensures centralized and secure identity management across all ERP modules.  
It is implemented using two **distinct and independent approaches**, deployed as **separate applications**:

---

### 🔑 a. Keycloak-Based Authentication

A robust and standards-compliant identity provider built on:

- OAuth2 and OpenID Connect
- Token issuance, login flows, and user session management handled by Keycloak
- Seamless integration with the **Admin Service**

---

### 🧬 b. Passwordless Blockchain Authentication

An innovative authentication solution using **blockchain** and **digital wallets**:

- Users authenticate using a wallet (e.g., **MetaMask**, **WalletConnect**)
- **Smart contracts** handle session validation and identity verification
- A dedicated **Authentification-Service** manages:
  - Wallet challenge generation
  - Public key verification
  - JWT token issuance upon successful verification

---

> ⚠️ **Note:** These two approaches are implemented as **separate applications** and are **not combined**.  
> Each is independently functional and addresses different use cases and levels of security.

## 🛡️ 2. Admin Service – Centralized Authorization & User Administration

### 🛠️ Technologies Used
- **Spring Boot**, **Spring Security**, **Spring Data JPA**, **Hibernate**, **Flyway**
- **PostgreSQL**

The **Admin Service** is the central component responsible for:

- Managing users, roles, and privileges across the ERP system.
- Verifying access rights at two levels: **API-level** and **data-level**.
- Enforcing authorization through a **hybrid model** combining:
  - **RBAC** (Role-Based Access Control)
  - **ABAC** (Attribute-Based Access Control)

### ✅ Key Features

- 🔐 Centralized management of user accounts, roles, and authorities
- 📄 Fine-grained access control (API & data layers)
- ⚙️ Hybrid access model (RBAC + ABAC)
- 🔎 Backend-side pagination, filtering, and searching
- 🧼 Developed with **Clean Architecture** and **SOLID principles**
- 💻 Clean Code, reusable logic, and clear structure

![Admin Service – Class Diagram](/Services/Admin-Service/admin_design.png)

---

## 📝 3. Audit Logging Service

### 🛠️ Technologies Used
- **Spring AOP**, **Spring Boot**
- **Apache Kafka**, **PostgreSQL**

The **Audit Logging Service** ensures consistent and centralized tracking of important events and actions across all ERP modules.

- Logs collected via **Spring AOP**
- Published asynchronously to **Kafka**
- Stored and managed in **AuditLog-Service**

---
## 📢 4. Notification Service – Multi-Channel Communication Engine

### 🛠️ Technologies Used
- **Spring Boot**, **Spring Kafka**
- **JavaMail / SMTP**, **Twilio API**, **WebSocket**, **Kafka**

A dedicated service responsible for sending real-time notifications via:

- 📧 Email
- 📱 SMS
- 🔔 Push / in-app alerts

Triggered through **Kafka events** from other modules.

---

## 🧭 5. Core Infrastructure Services

### 🛠️ Technologies Used
- **Spring Cloud**, **Spring Gateway**, **Spring Config**, **Eureka**

- **Config Service** – centralized configuration (Spring Cloud Config)
- **API Gateway** – routing & filtering (Spring Cloud Gateway)
- **Discovery Service** – service registry (Netflix Eureka)

---

## 🖥️ 6. Front-End Interface – Fast, Clean & Intuitive UI

### 🛠️ Technologies Used
- **Angular**, **PrimeNG**
---

## 📦 7. DevOps – Containerization & Observability

### 🛠️ Technologies Used
- **Docker**, **Docker Compose**
- **Prometheus**, **Grafana**, **Loki**, **Tempo**
- **Spring Boot Actuator**
---
