#!/bin/bash

# Kill any process using port 8000
lsof -ti:8000 | xargs kill -9 2>/dev/null

# Build and run the application
mvn clean package
mvn tomcat7:run

echo "Server is running at http://localhost:8000"
