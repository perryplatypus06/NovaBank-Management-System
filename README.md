# 🏦 NovaBank Management System

A multi-user desktop banking application built using Java Swing and MySQL database storage. Features dynamic UI theme switching, real-time transaction ledger updates, and transactional security.

---

## ✨ Features

- **Multi-User Authentication**: Secure Login and Account Registration module.
- **Isolated User Sessions**: Each account maintains independent balances and transaction records.
- **Real-time Ledger Table**: Modern `JTable` rendering transaction logs with formatted timestamps.
- **Dynamic Theming**: Light/Dark mode toggle for full UI workspace customization.
- **Data Persistence**: Integrated via MySQL Server with double-entry relational database design.

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
