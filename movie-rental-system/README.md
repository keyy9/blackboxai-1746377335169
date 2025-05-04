# Movie Rental System

A modern web-based movie rental management system built with Java, MySQL, and a responsive web interface using Tailwind CSS.

## Features

- User Management
  - Register new users
  - View and edit user information
  - Track user rental history

- Movie Management
  - Add and manage movies
  - Track available copies
  - Set movie pricing categories

- Rental Management
  - Create new rentals
  - Track due dates
  - Handle returns and late fees

- Pricing System
  - Flexible pricing categories
  - Configurable late fee rules
  - Dynamic price calculation

- Reports
  - Revenue tracking
  - Popular movies
  - Overdue rentals

## Technology Stack

- Backend:
  - Java 11
  - Jakarta EE 9
  - MySQL Database
  - Maven for dependency management

- Frontend:
  - HTML5
  - Tailwind CSS
  - JavaScript
  - Font Awesome icons

## Setup Instructions

1. Database Setup:
   ```bash
   # Create database and tables
   mysql -u root -p < src/main/resources/database.sql
   ```

2. Configure Database Connection:
   - Open `src/main/java/util/DatabaseConnection.java`
   - Update the following constants with your database credentials:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/movie_rental_system";
     private static final String USER = "your_username";
     private static final String PASSWORD = "your_password";
     ```

3. Build the Project:
   ```bash
   mvn clean install
   ```

4. Run the Application:
   ```bash
   mvn tomcat7:run
   ```

5. Access the Application:
   - Open your browser and navigate to: `http://localhost:8000`

## Project Structure

```
movie-rental-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── dao/         # Data Access Objects
│   │   │   ├── model/       # Entity classes
│   │   │   ├── util/        # Utility classes
│   │   │   └── web/         # Servlets and filters
│   │   ├── resources/
│   │   │   └── database.sql # Database schema
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       ├── error/       # Error pages
│   │       ├── index.html   # Main application page
│   │       ├── styles.css   # Tailwind styles
│   │       └── app.js       # Frontend logic
└── pom.xml                  # Maven configuration
```

## Database Schema

```sql
Tables:
- users (id, name, email, phone)
- movies (id, title, genre, available_copies)
- pricing_categories (id, name, base_price)
- movie_pricing (id, movie_id, pricing_category_id)
- late_fees (id, days_late_start, days_late_end, fee_per_day)
- rentals (id, user_id, movie_id, rental_date, due_date, return_date, total_price)
```

## API Endpoints

- Users:
  - GET /api/users - List all users
  - POST /api/users - Create new user
  - PUT /api/users/{id} - Update user
  - DELETE /api/users/{id} - Delete user

- Movies:
  - GET /api/movies - List all movies
  - POST /api/movies - Add new movie
  - PUT /api/movies/{id} - Update movie
  - DELETE /api/movies/{id} - Delete movie

- Rentals:
  - GET /api/rentals - List all rentals
  - GET /api/rentals/active - List active rentals
  - POST /api/rentals - Create new rental
  - PUT /api/rentals/return/{id} - Return movie

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
