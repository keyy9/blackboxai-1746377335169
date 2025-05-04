# Movie Rental System Demo

A modern, interactive demo of the Movie Rental System with a beautiful UI built using Tailwind CSS.

## Quick Start

1. Start the server:
```bash
cd movie-rental-system/demo
python3 -m http.server 8000
```

2. Open in browser:
   - Navigate to http://localhost:8000

## Features

### Dashboard
- View total movies, active users, and rental statistics
- Track active and overdue rentals
- See recent rental activity with status indicators

### Movies
- Browse movie catalog with movie posters
- See real-time availability status
- View pricing for each movie
- Rent movies with one click

### Users
- View registered users
- Track active rentals per user
- Manage user accounts

### Rentals
- View all active rentals
- Track due dates
- Process returns
- Monitor overdue status

## Sample Data

The demo comes with sample data including:
- Popular movies with posters
- Sample users
- Active rentals

## Technology Stack

- HTML5
- Tailwind CSS for styling
- JavaScript for interactivity
- LocalStorage for data persistence
- Font Awesome icons
- Google Fonts (Poppins)

## Testing the Demo

1. Navigate through sections:
   - Click menu items in the sidebar
   - Watch smooth page transitions

2. Try renting a movie:
   - Go to "Movies" section
   - Click "Rent" on an available movie
   - Check Dashboard for updated stats
   - View the rental in "Rentals" section

3. Return a movie:
   - Go to "Rentals" section
   - Find an active rental
   - Click "Return"
   - Watch stats update automatically

4. Check features:
   - Real-time statistics updates
   - Persistent data (refreshing keeps state)
   - Responsive design (try different screen sizes)
   - Interactive notifications

## Notes

- Data persists in localStorage
- All changes are temporary and reset when clearing browser data
- Images are loaded from IMDB's public URLs
- Designed to work in modern browsers
