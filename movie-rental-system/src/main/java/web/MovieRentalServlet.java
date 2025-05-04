package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.google.gson.Gson;
import dao.*;
import model.*;

@WebServlet("/api/*")
public class MovieRentalServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final MovieDAO movieDAO = new MovieDAO();
    private final RentalDAO rentalDAO = new RentalDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if ("/users".equals(pathInfo)) {
                List<User> users = userDAO.getAll();
                response.getWriter().write(gson.toJson(users));
            } 
            else if ("/movies".equals(pathInfo)) {
                List<Movie> movies = movieDAO.getAll();
                response.getWriter().write(gson.toJson(movies));
            }
            else if ("/rentals".equals(pathInfo)) {
                List<Rental> rentals = rentalDAO.getAll();
                response.getWriter().write(gson.toJson(rentals));
            }
            else if ("/active-rentals".equals(pathInfo)) {
                List<Rental> activeRentals = rentalDAO.getActiveRentals();
                response.getWriter().write(gson.toJson(activeRentals));
            }
            else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if ("/users".equals(pathInfo)) {
                // Create new user
                User user = gson.fromJson(request.getReader(), User.class);
                user = userDAO.create(user);
                response.getWriter().write(gson.toJson(user));
            }
            else if ("/movies".equals(pathInfo)) {
                // Create new movie
                Movie movie = gson.fromJson(request.getReader(), Movie.class);
                movie = movieDAO.create(movie, 1); // Default pricing category
                response.getWriter().write(gson.toJson(movie));
            }
            else if ("/rentals".equals(pathInfo)) {
                // Create new rental
                Rental rental = gson.fromJson(request.getReader(), Rental.class);
                rental = rentalDAO.create(rental);
                response.getWriter().write(gson.toJson(rental));
            }
            else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.startsWith("/users/")) {
                // Update user
                int userId = Integer.parseInt(pathInfo.substring(7));
                User user = gson.fromJson(request.getReader(), User.class);
                user.setId(userId);
                boolean updated = userDAO.update(user);
                response.getWriter().write("{\"success\": " + updated + "}");
            }
            else if (pathInfo.startsWith("/movies/")) {
                // Update movie
                int movieId = Integer.parseInt(pathInfo.substring(8));
                Movie movie = gson.fromJson(request.getReader(), Movie.class);
                movie.setId(movieId);
                boolean updated = movieDAO.update(movie, null);
                response.getWriter().write("{\"success\": " + updated + "}");
            }
            else if (pathInfo.startsWith("/rentals/return/")) {
                // Return rental
                int rentalId = Integer.parseInt(pathInfo.substring(15));
                Rental rental = gson.fromJson(request.getReader(), Rental.class);
                boolean updated = rentalDAO.returnMovie(rentalId, rental.getReturnDate(), rental.getLateFee());
                response.getWriter().write("{\"success\": " + updated + "}");
            }
            else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.startsWith("/users/")) {
                int userId = Integer.parseInt(pathInfo.substring(7));
                boolean deleted = userDAO.delete(userId);
                response.getWriter().write("{\"success\": " + deleted + "}");
            }
            else if (pathInfo.startsWith("/movies/")) {
                int movieId = Integer.parseInt(pathInfo.substring(8));
                boolean deleted = movieDAO.delete(movieId);
                response.getWriter().write("{\"success\": " + deleted + "}");
            }
            else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
