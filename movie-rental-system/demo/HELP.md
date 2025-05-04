# Movie Rental System Demo - Help Guide

## Quick Start
1. Open http://localhost:8000 in your browser
2. Navigate using the sidebar menu
3. Try out the features!

## Features Guide

### Dashboard
- View total movies, active users, and rental statistics
- Track active and overdue rentals
- See recent rental activity with status indicators
- Statistics update in real-time

### Movies Section
1. Browse Movies:
   - View movie posters and details
   - Check availability status
   - See rental prices

2. Rent a Movie:
   - Click the "Rent" button on any available movie
   - The movie will be marked as unavailable
   - Dashboard stats will update automatically
   - A new rental record will appear in Recent Rentals

### Rentals Section
1. View Active Rentals:
   - See all current rentals
   - Check due dates
   - Monitor overdue status

2. Return a Movie:
   - Find the rental in the list
   - Click "Return" button
   - Movie becomes available again
   - Dashboard updates automatically

### Users Section
- View registered users
- See active rentals per user
- Manage user accounts

## Sample Data
The demo comes with:
- 5 popular movies with posters:
  * The Dark Knight ($5.00)
  * Inception ($4.50)
  * The Godfather ($3.50)
  * Pulp Fiction ($4.00)
  * The Shawshank Redemption ($3.50)
- 2 sample users:
  * John Doe (john@example.com)
  * Jane Smith (jane@example.com)

## Tips & Tricks
1. Data Persistence:
   - All changes are saved in your browser's localStorage
   - Data remains after page refresh
   - Clear browser data to reset the demo

2. Status Indicators:
   - Green: Active rental
   - Red: Overdue rental
   - Gray: Returned

3. Notifications:
   - Success messages appear in green
   - Error messages appear in red
   - Messages auto-dismiss after 3 seconds

## Troubleshooting
1. If the page doesn't load:
   - Check that the server is running (setup.sh)
   - Verify you're using http://localhost:8000
   - Try refreshing the page

2. If data seems incorrect:
   - Clear your browser's localStorage
   - Refresh the page to reset to default data

3. If images don't load:
   - Check your internet connection
   - Movie posters are loaded from IMDB

## Server Management
1. Start the server:
   ```bash
   ./setup.sh
   ```

2. Stop the server:
   - Press Ctrl+C in the terminal
   - Or run: pkill -f "python3 -m http.server"

3. Restart the server:
   - Run setup.sh again
