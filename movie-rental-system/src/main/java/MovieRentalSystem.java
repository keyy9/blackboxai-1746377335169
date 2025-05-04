import dao.*;
import model.*;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MovieRentalSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();
    private static final MovieDAO movieDAO = new MovieDAO();
    private static final PricingCategoryDAO pricingCategoryDAO = new PricingCategoryDAO();
    private static final LateFeeDAO lateFeeDAO = new LateFeeDAO();
    private static final RentalDAO rentalDAO = new RentalDAO();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        try {
            if (!DatabaseConnection.testConnection()) {
                System.out.println("Failed to connect to database. Please check your connection settings.");
                return;
            }
            
            while (true) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        userManagementMenu();
                        break;
                    case 2:
                        movieManagementMenu();
                        break;
                    case 3:
                        pricingManagementMenu();
                        break;
                    case 4:
                        rentalManagementMenu();
                        break;
                    case 5:
                        reportsMenu();
                        break;
                    case 6:
                        System.out.println("Thank you for using Movie Rental System!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Movie Rental System ===");
        System.out.println("1. User Management");
        System.out.println("2. Movie Management");
        System.out.println("3. Pricing Management");
        System.out.println("4. Rental Management");
        System.out.println("5. Reports");
        System.out.println("6. Exit");
    }

    private static void userManagementMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== User Management ===");
            System.out.println("1. Register New User");
            System.out.println("2. View Users");
            System.out.println("3. Update User");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    viewUsers();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void movieManagementMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== Movie Management ===");
            System.out.println("1. Add New Movie");
            System.out.println("2. View Movies");
            System.out.println("3. Update Movie");
            System.out.println("4. Set Movie Pricing");
            System.out.println("5. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    viewMovies();
                    break;
                case 3:
                    updateMovie();
                    break;
                case 4:
                    setMoviePricing();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void pricingManagementMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== Pricing Management ===");
            System.out.println("1. Manage Pricing Categories");
            System.out.println("2. Manage Late Fees");
            System.out.println("3. View Pricing Reports");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    managePricingCategories();
                    break;
                case 2:
                    manageLateFees();
                    break;
                case 3:
                    viewPricingReports();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void rentalManagementMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== Rental Management ===");
            System.out.println("1. Rent Movie");
            System.out.println("2. Return Movie");
            System.out.println("3. View Active Rentals");
            System.out.println("4. View Rental History");
            System.out.println("5. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    rentMovie();
                    break;
                case 2:
                    returnMovie();
                    break;
                case 3:
                    viewActiveRentals();
                    break;
                case 4:
                    viewRentalHistory();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void reportsMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== Reports ===");
            System.out.println("1. Revenue Report");
            System.out.println("2. Late Returns Report");
            System.out.println("3. Popular Movies Report");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    generateRevenueReport();
                    break;
                case 2:
                    generateLateReturnsReport();
                    break;
                case 3:
                    generatePopularMoviesReport();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // User Management Methods
    private static void registerUser() throws SQLException {
        System.out.println("\n=== Register New User ===");
        
        String name = getStringInput("Enter name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");

        if (userDAO.emailExists(email)) {
            System.out.println("Email already exists!");
            return;
        }

        User user = new User(name, email, phone);
        user = userDAO.create(user);
        System.out.println("User registered successfully! ID: " + user.getId());
    }

    private static void viewUsers() throws SQLException {
        System.out.println("\n=== User List ===");
        List<User> users = userDAO.getAll();
        
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void updateUser() throws SQLException {
        System.out.println("\n=== Update User ===");
        int userId = getIntInput("Enter user ID: ");
        
        User user = userDAO.getById(userId);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("Current user details: " + user);
        
        String name = getStringInput("Enter new name (or press Enter to keep current): ");
        String email = getStringInput("Enter new email (or press Enter to keep current): ");
        String phone = getStringInput("Enter new phone (or press Enter to keep current): ");

        if (!name.isEmpty()) user.setName(name);
        if (!email.isEmpty()) user.setEmail(email);
        if (!phone.isEmpty()) user.setPhone(phone);

        if (userDAO.update(user)) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("Failed to update user.");
        }
    }

    // Movie Management Methods
    private static void addMovie() throws SQLException {
        System.out.println("\n=== Add New Movie ===");
        
        String title = getStringInput("Enter title: ");
        String genre = getStringInput("Enter genre: ");
        int copies = getIntInput("Enter number of copies: ");

        // Show pricing categories
        List<PricingCategory> categories = pricingCategoryDAO.getAll();
        System.out.println("\nAvailable Pricing Categories:");
        for (PricingCategory category : categories) {
            System.out.println(category.getId() + ". " + category.getName() + 
                             " (Price: $" + category.getBasePrice() + ")");
        }

        int categoryId = getIntInput("Select pricing category ID: ");
        PricingCategory category = pricingCategoryDAO.getById(categoryId);
        if (category == null) {
            System.out.println("Invalid pricing category!");
            return;
        }

        Movie movie = new Movie(title, genre, copies);
        movie = movieDAO.create(movie, categoryId);
        System.out.println("Movie added successfully! ID: " + movie.getId());
    }

    private static void viewMovies() throws SQLException {
        System.out.println("\n=== Movie List ===");
        List<Movie> movies = movieDAO.getAll();
        
        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }

    private static void updateMovie() throws SQLException {
        System.out.println("\n=== Update Movie ===");
        int movieId = getIntInput("Enter movie ID: ");
        
        Movie movie = movieDAO.getById(movieId);
        if (movie == null) {
            System.out.println("Movie not found!");
            return;
        }

        System.out.println("Current movie details: " + movie);
        
        String title = getStringInput("Enter new title (or press Enter to keep current): ");
        String genre = getStringInput("Enter new genre (or press Enter to keep current): ");
        String copies = getStringInput("Enter new number of copies (or press Enter to keep current): ");

        if (!title.isEmpty()) movie.setTitle(title);
        if (!genre.isEmpty()) movie.setGenre(genre);
        if (!copies.isEmpty()) movie.setAvailableCopies(Integer.parseInt(copies));

        if (movieDAO.update(movie, null)) {
            System.out.println("Movie updated successfully!");
        } else {
            System.out.println("Failed to update movie.");
        }
    }

    private static void setMoviePricing() throws SQLException {
        System.out.println("\n=== Set Movie Pricing ===");
        int movieId = getIntInput("Enter movie ID: ");
        
        Movie movie = movieDAO.getById(movieId);
        if (movie == null) {
            System.out.println("Movie not found!");
            return;
        }

        // Show pricing categories
        List<PricingCategory> categories = pricingCategoryDAO.getAll();
        System.out.println("\nAvailable Pricing Categories:");
        for (PricingCategory category : categories) {
            System.out.println(category.getId() + ". " + category.getName() + 
                             " (Price: $" + category.getBasePrice() + ")");
        }

        int categoryId = getIntInput("Select new pricing category ID: ");
        PricingCategory category = pricingCategoryDAO.getById(categoryId);
        if (category == null) {
            System.out.println("Invalid pricing category!");
            return;
        }

        if (movieDAO.update(movie, categoryId)) {
            System.out.println("Movie pricing updated successfully!");
        } else {
            System.out.println("Failed to update movie pricing.");
        }
    }

    // Rental Management Methods
    private static void rentMovie() throws SQLException {
        System.out.println("\n=== Rent Movie ===");
        
        // Show available movies
        List<Movie> movies = movieDAO.getAvailableMovies();
        if (movies.isEmpty()) {
            System.out.println("No movies available for rent.");
            return;
        }

        System.out.println("\nAvailable Movies:");
        for (Movie movie : movies) {
            System.out.println(movie.getId() + ". " + movie.getTitle() + 
                             " (Price: $" + movie.getCurrentPrice() + ")");
        }

        int movieId = getIntInput("Select movie ID: ");
        Movie movie = movieDAO.getById(movieId);
        if (movie == null || !movie.isAvailable()) {
            System.out.println("Invalid movie selection or movie not available!");
            return;
        }

        // Show users
        List<User> users = userDAO.getAll();
        System.out.println("\nSelect User:");
        for (User user : users) {
            System.out.println(user.getId() + ". " + user.getName());
        }

        int userId = getIntInput("Select user ID: ");
        User user = userDAO.getById(userId);
        if (user == null) {
            System.out.println("Invalid user selection!");
            return;
        }

        // Set rental dates
        LocalDate rentalDate = LocalDate.now();
        LocalDate dueDate = rentalDate.plusDays(7); // 7 days rental period

        // Create rental
        Rental rental = new Rental(userId, movieId, rentalDate, dueDate, movie.getCurrentPrice());
        rental = rentalDAO.create(rental);

        System.out.println("Movie rented successfully!");
        System.out.println("Due date: " + dueDate.format(DATE_FORMATTER));
        System.out.println("Rental price: $" + rental.getBasePrice());
    }

    private static void returnMovie() throws SQLException {
        System.out.println("\n=== Return Movie ===");
        
        // Show active rentals
        List<Rental> activeRentals = rentalDAO.getActiveRentals();
        if (activeRentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }

        System.out.println("\nActive Rentals:");
        for (Rental rental : activeRentals) {
            System.out.println(rental.getId() + ". Movie: " + rental.getMovieTitle() + 
                             ", User: " + rental.getUserName() + 
                             ", Due: " + rental.getDueDate().format(DATE_FORMATTER));
        }

        int rentalId = getIntInput("Select rental ID to return: ");
        Rental rental = rentalDAO.getById(rentalId);
        if (rental == null || rental.isReturned()) {
            System.out.println("Invalid rental selection or already returned!");
            return;
        }

        LocalDate returnDate = LocalDate.now();
        int daysLate = rental.getDaysLate();
        BigDecimal lateFee = lateFeeDAO.calculateLateFee(daysLate);

        if (rentalDAO.returnMovie(rentalId, returnDate, lateFee)) {
            System.out.println("Movie returned successfully!");
            if (lateFee.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("Late fee charged: $" + lateFee);
            }
        } else {
            System.out.println("Failed to return movie.");
        }
    }

    private static void viewActiveRentals() throws SQLException {
        System.out.println("\n=== Active Rentals ===");
        List<Rental> rentals = rentalDAO.getActiveRentals();
        
        if (rentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }

        for (Rental rental : rentals) {
            System.out.println("Rental ID: " + rental.getId());
            System.out.println("Movie: " + rental.getMovieTitle());
            System.out.println("User: " + rental.getUserName());
            System.out.println("Due Date: " + rental.getDueDate().format(DATE_FORMATTER));
            System.out.println("Base Price: $" + rental.getBasePrice());
            System.out.println("------------------------");
        }
    }

    private static void viewRentalHistory() throws SQLException {
        System.out.println("\n=== Rental History ===");
        List<Rental> rentals = rentalDAO.getAll();
        
        if (rentals.isEmpty()) {
            System.out.println("No rental history found.");
            return;
        }

        for (Rental rental : rentals) {
            System.out.println("Rental ID: " + rental.getId());
            System.out.println("Movie: " + rental.getMovieTitle());
            System.out.println("User: " + rental.getUserName());
            System.out.println("Rental Date: " + rental.getRentalDate().format(DATE_FORMATTER));
            System.out.println("Due Date: " + rental.getDueDate().format(DATE_FORMATTER));
            if (rental.getReturnDate() != null) {
                System.out.println("Return Date: " + rental.getReturnDate().format(DATE_FORMATTER));
            }
            System.out.println("Total Price: $" + rental.getTotalPrice());
            System.out.println("------------------------");
        }
    }

    // Report Generation Methods
    private static void generateRevenueReport() throws SQLException {
        System.out.println("\n=== Revenue Report ===");
        // TODO: Implement revenue report generation
        System.out.println("Revenue report generation not implemented yet.");
    }

    private static void generateLateReturnsReport() throws SQLException {
        System.out.println("\n=== Late Returns Report ===");
        List<Rental> overdueRentals = rentalDAO.getOverdueRentals();
        
        if (overdueRentals.isEmpty()) {
            System.out.println("No overdue rentals found.");
            return;
        }

        for (Rental rental : overdueRentals) {
            System.out.println("Rental ID: " + rental.getId());
            System.out.println("Movie: " + rental.getMovieTitle());
            System.out.println("User: " + rental.getUserName());
            System.out.println("Due Date: " + rental.getDueDate().format(DATE_FORMATTER));
            System.out.println("Days Overdue: " + rental.getDaysLate());
            System.out.println("------------------------");
        }
    }

    private static void generatePopularMoviesReport() throws SQLException {
        System.out.println("\n=== Popular Movies Report ===");
        // TODO: Implement popular movies report generation
        System.out.println("Popular movies report generation not implemented yet.");
    }

    // Utility Methods
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (YYYY-MM-DD): ");
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Please enter a valid date in the format YYYY-MM-DD.");
            }
        }
    }

    private static BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
