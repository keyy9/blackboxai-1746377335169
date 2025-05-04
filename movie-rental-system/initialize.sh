#!/bin/bash

echo "Initializing Movie Rental System..."

# Stop any process using port 8000
echo "Checking for processes using port 8000..."
lsof -ti:8000 | xargs kill -9 2>/dev/null

# Read database configuration
if [ -f "config/database.properties" ]; then
    DB_USER=$(grep "db.user" config/database.properties | cut -d'=' -f2)
    DB_PASS=$(grep "db.password" config/database.properties | cut -d'=' -f2)
else
    echo "Using default database credentials..."
    DB_USER="root"
    DB_PASS=""
fi

# Initialize database
echo "Setting up database..."
mysql -u"$DB_USER" -p"$DB_PASS" < init-database.sql

if [ $? -eq 0 ]; then
    echo "Database initialized successfully!"
    
    # Build the project
    echo "Building project..."
    mvn clean package
    
    if [ $? -eq 0 ]; then
        echo "Project built successfully!"
        echo ""
        echo "Starting the application..."
        mvn tomcat7:run &
        
        # Wait for server to start
        echo "Waiting for server to start..."
        sleep 5
        
        echo ""
        echo "Application is now running!"
        echo "Open http://localhost:8000 in your browser"
        echo ""
        echo "Sample users:"
        echo "- Email: john@example.com"
        echo "- Email: jane@example.com"
        echo "- Email: bob@example.com"
        echo ""
        echo "To stop the server, press Ctrl+C"
        
        # Wait for Ctrl+C
        wait
    else
        echo "Project build failed. Please check the error messages above."
    fi
else
    echo "Database initialization failed. Please check:"
    echo "1. Is MySQL running?"
    echo "2. Are the database credentials correct in config/database.properties?"
    echo "3. Do you have sufficient privileges?"
fi
