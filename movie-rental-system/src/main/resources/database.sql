-- Create database
CREATE DATABASE IF NOT EXISTS movie_rental_system;
USE movie_rental_system;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_email UNIQUE (email)
);

-- Create pricing_categories table
CREATE TABLE IF NOT EXISTS pricing_categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_category_name UNIQUE (name)
);

-- Create movies table
CREATE TABLE IF NOT EXISTS movies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    available_copies INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create movie_pricing table
CREATE TABLE IF NOT EXISTS movie_pricing (
    id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT NOT NULL,
    pricing_category_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (pricing_category_id) REFERENCES pricing_categories(id)
);

-- Create late_fees table
CREATE TABLE IF NOT EXISTS late_fees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    days_late_start INT NOT NULL,
    days_late_end INT NOT NULL,
    fee_per_day DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create rentals table
CREATE TABLE IF NOT EXISTS rentals (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    rental_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    base_price DECIMAL(10,2) NOT NULL,
    late_fee DECIMAL(10,2) DEFAULT 0.00,
    total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- Insert default pricing categories
INSERT INTO pricing_categories (name, base_price) VALUES
('New Release', 5.00),
('Regular', 3.50),
('Classic', 2.00);

-- Insert default late fee structure
INSERT INTO late_fees (days_late_start, days_late_end, fee_per_day) VALUES
(1, 3, 1.00),
(4, 7, 2.00),
(8, 999999, 3.00);
