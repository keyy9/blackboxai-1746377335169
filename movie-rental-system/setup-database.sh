#!/bin/bash

# MySQL database configuration
DB_NAME="movie_rental_system"
DB_USER="root"
DB_PASS=""

# Create database and import schema
mysql -u $DB_USER --execute="CREATE DATABASE IF NOT EXISTS $DB_NAME;"
mysql -u $DB_USER $DB_NAME < src/main/resources/database.sql

echo "Database setup completed!"
