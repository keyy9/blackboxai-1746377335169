-- Create database if not exists
CREATE DATABASE IF NOT EXISTS movie_rental_system;
USE movie_rental_system;

-- Import schema first
SOURCE src/main/resources/database.sql;

-- Insert sample data
-- Pricing Categories
INSERT INTO pricing_categories (name, base_price) VALUES 
('New Release', 5.00),
('Regular', 3.50),
('Classic', 2.00);

-- Late Fee Rules
INSERT INTO late_fees (days_late_start, days_late_end, fee_per_day) VALUES 
(1, 3, 1.00),
(4, 7, 2.00),
(8, 999999, 3.00);

-- Sample Movies
INSERT INTO movies (title, genre, available_copies) VALUES 
('The Dark Knight', 'Action', 5),
('Inception', 'Sci-Fi', 3),
('The Godfather', 'Drama', 2),
('Pulp Fiction', 'Crime', 4),
('The Shawshank Redemption', 'Drama', 3);

-- Link movies to pricing categories
INSERT INTO movie_pricing (movie_id, pricing_category_id) 
SELECT m.id, pc.id 
FROM movies m 
CROSS JOIN pricing_categories pc 
WHERE pc.name = 'Regular';

-- Sample Users
INSERT INTO users (name, email, phone) VALUES 
('John Doe', 'john@example.com', '123-456-7890'),
('Jane Smith', 'jane@example.com', '234-567-8901'),
('Bob Wilson', 'bob@example.com', '345-678-9012');

-- Display results
SELECT 'Database initialized successfully!' as Message;
SELECT COUNT(*) as 'Number of Movies' FROM movies;
SELECT COUNT(*) as 'Number of Users' FROM users;
SELECT COUNT(*) as 'Number of Pricing Categories' FROM pricing_categories;
