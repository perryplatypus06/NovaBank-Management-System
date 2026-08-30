CREATE DATABASE IF NOT EXISTS novabank;
USE novabank;

-- Drop existing tables to update structure safely
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

-- 1. Create Users Table
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Create Accounts Table Linked to User
CREATE TABLE accounts (
                          account_id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT UNIQUE NOT NULL,
                          account_number VARCHAR(20) UNIQUE NOT NULL,
                          balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Create Transactions Table
CREATE TABLE transactions (
                              transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                              account_id INT NOT NULL,
                              type ENUM('DEPOSIT', 'WITHDRAW') NOT NULL,
                              amount DECIMAL(10,2) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Seed default user (Username: admin | Password: password123)
INSERT INTO users (user_id, username, password, full_name)
VALUES (1, 'admin', 'password123', 'Default Admin');

INSERT INTO accounts (account_id, user_id, account_number, balance)
VALUES (1, 1, 'NB-1001', 0.00);