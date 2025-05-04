#!/bin/bash

echo "Testing database connection..."

# Read database configuration
if [ -f "config/database.properties" ]; then
    DB_URL=$(grep "db.url" config/database.properties | cut -d'=' -f2)
    DB_USER=$(grep "db.user" config/database.properties | cut -d'=' -f2)
    DB_PASS=$(grep "db.password" config/database.properties | cut -d'=' -f2)
else
    echo "Database configuration file not found!"
    exit 1
fi

# Test MySQL connection
if mysql -u"$DB_USER" -p"$DB_PASS" -e "SELECT 1;" > /dev/null 2>&1; then
    echo "MySQL connection successful!"
    
    # Test if database exists
    DB_NAME=$(echo $DB_URL | awk -F'/' '{print $NF}')
    if mysql -u"$DB_USER" -p"$DB_PASS" -e "USE $DB_NAME;" > /dev/null 2>&1; then
        echo "Database '$DB_NAME' exists!"
        
        # Test if tables exist
        TABLES=$(mysql -u"$DB_USER" -p"$DB_PASS" -D"$DB_NAME" -e "SHOW TABLES;" 2>/dev/null)
        if [ ! -z "$TABLES" ]; then
            echo "Database tables found:"
            echo "$TABLES"
            echo "Database setup appears to be complete!"
            exit 0
        else
            echo "No tables found in database. Please run setup-database.sh"
            exit 1
        fi
    else
        echo "Database '$DB_NAME' not found. Please run setup-database.sh"
        exit 1
    fi
else
    echo "Failed to connect to MySQL. Please check:"
    echo "1. Is MySQL running?"
    echo "2. Are the credentials in config/database.properties correct?"
    exit 1
fi
