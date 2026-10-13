
# 🏦 NovaBank Management System

A multi-user desktop banking application built using Java Swing and MySQL database storage. Features dynamic UI theme switching, real-time transaction ledger updates, and transactional security.

---

## ✨ Features

- **Multi-User Authentication**: Secure Login and Account Registration module.
- **Isolated User Sessions**: Each account maintains independent balances and transaction records.
- **Real-time Ledger Table**: Modern `JTable` rendering transaction logs with formatted timestamps.
- **Dynamic Theming**: Light/Dark mode toggle for full UI workspace customization.
- **Data Persistence**: Integrated via MySQL Server with double-entry relational database design.
=======
# 🚀 NovaBank Management System

> A secure, modern desktop banking application built with **Java Swing**, **JDBC**, and **MySQL**, featuring enterprise-grade security implementations (BCrypt hashing, brute-force rate limiting, and real-time password strength validation) paired with a sleek fintech UI.

---

## ✨ Key Features & Architecture

### 🔒 1. Advanced Cybersecurity & Defense-in-Depth
* **BCrypt Password Hashing:** User passwords are never stored in plaintext. Uses unique salts and secure work factors via `jbcrypt`.
* **Lazy Migration:** Automatically upgrades legacy plaintext database credentials to secure BCrypt hashes transparently on first login.
* **Brute-Force Rate Limiting & Lockout:** Built-in tracker (`LoginAttemptTracker`) locks accounts temporarily after 3 consecutive failed login attempts.
* **Password Policy Enforcement:** Real-time feedback engine (`PasswordValidator`) enforcing uppercase letters, numbers, symbols, and checking against common-password blacklists.

### 🎨 2. Modern Fintech UI Redesign
* **Split-Screen Authentication:** Clean corporate brand panel alongside an interactive login/registration form with a live **Show/Hide password toggle**.
* **Card-Based Dashboard:** Modern aesthetic inspired by top fintech platforms featuring a purple gradient virtual debit card widget, available balance card, and quick-action grid (Deposit, Withdraw, Transfer, Passbook).
* **Live Activity Feed:** Real-time recent transaction tracking with color-coded credits (green `+`) and debits (red `-`).
>>>>>>> 1b363fb (feat: complete fintech UI modernization, add transaction history feed and enterprise security)

---

## 🛠️ Tech Stack


- **Language**: Java 17+
- **GUI Framework**: Java Swing
- **Database**: MySQL Server 8.0
- **Database Connectivity**: JDBC (`mysql-connector-j`)

---

## 🚀 Quick Setup Guide

### 1. Database Setup
1. Open **MySQL Workbench**.
2. Run the SQL script provided in [`schema.sql`](./schema.sql) to initialize the database schema and default tables.

### 2. Configure Database Credentials
Open `src/Database.java` and update your MySQL connection credentials:
```java
private static final String URL = "jdbc:mysql://localhost:3306/novabank";
private static final String USER = "root";
private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";
=======
* **Language:** Java 17+
* **GUI Framework:** Java Swing (AWT, Custom Layouts & Gradients)
* **Database & Connectivity:** MySQL 8.0, JDBC, PreparedStatements (SQL Injection Prevention)
* **Security Library:** `jbcrypt-0.4.jar`

---

## ⚙️ Database & Test Setup

1. **Create the Database:**
   ```sql
   CREATE DATABASE novabank;
   USE novabank;
>>>>>>> 1b363fb (feat: complete fintech UI modernization, add transaction history feed and enterprise security)
