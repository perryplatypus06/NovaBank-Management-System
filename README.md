
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
* <img width="961" height="591" alt="Screenshot 2026-10-13 182407" src="https://github.com/user-attachments/assets/b08e23ad-152d-4c50-a086-fdb951327e44" />

<img width="1127" height="802" alt="Screenshot 2026-10-13 182810" src="https://github.com/user-attachments/assets/30473b36-a964-4d32-af4f-458690ccdd5d" />
<img width="1122" height="808" alt="Screenshot 2026-10-13 182638" src="https://github.com/user-attachments/assets/3f6b3b99-b7a2-41c3-be79-7fffedb5e31f" />
<img width="1132" height="811" alt="Screenshot 2026-10-13 182605" src="https://github.com/user-attachments/assets/c411444b-52cd-4c88-b79b-f4235c504df3" />
<img width="1127" height="807" alt="Screenshot 2026-10-13 182543" src="https://github.com/user-attachments/assets/afcc7570-2567-48cd-9d83-e5ad46ebf469" />
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
---
======






