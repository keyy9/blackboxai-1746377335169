# Quick Start Guide

## Setup Instructions

1. First, stop any running servers and initialize the system:
```bash
# Navigate to the project directory
cd movie-rental-system

# Make scripts executable
chmod +x initialize.sh test-database.sh

# Initialize the system (sets up database and starts the server)
./initialize.sh
```

2. Open http://localhost:8000 in your browser

3. Use these sample accounts to test the system:
- john@example.com
- jane@example.com
- bob@example.com

## Features Available

1. Movie Catalog:
- Browse all movies
- Check availability
- View pricing

2. User Management:
- Register new users
- Update user information
- View rental history

3. Rental Operations:
- Rent movies
- Return movies
- View active rentals
- Check late fees

4. Pricing System:
- Different categories (New Release, Regular, Classic)
- Automatic late fee calculation
- Flexible pricing rules

## Troubleshooting

If you encounter issues:

1. Database Connection:
```bash
# Test database connection
./test-database.sh
```

2. Port in Use:
```bash
# Kill process using port 8000
lsof -ti:8000 | xargs kill -9
```

3. Clean Start:
```bash
# Stop any running servers
./initialize.sh
```

## Development

To modify the system:

1. Edit database settings:
```bash
nano config/database.properties
```

2. Build manually:
```bash
mvn clean package
```

3. Run server:
```bash
mvn tomcat7:run
```

For more detailed information, see the full README.md file.
