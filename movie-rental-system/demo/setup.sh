#!/bin/bash

echo "Movie Rental System Demo Setup"
echo "-----------------------------"

# Kill any running Python server and wait
echo "Stopping any running servers..."
pkill -f "python3 -m http.server" 2>/dev/null
sleep 2

# Clean up any temporary files
echo "Cleaning up temporary files..."
rm -f app2.js index2.html styles.css movies.json

# Verify directory structure
echo "Verifying directory structure..."
for dir in css js data; do
    if [ ! -d "$dir" ]; then
        echo "Creating $dir directory..."
        mkdir -p "$dir"
    fi
done

# Move files to correct locations if they exist in wrong places
echo "Organizing files..."
[ -f "styles.css" ] && mv styles.css css/ 2>/dev/null
[ -f "app.js" ] && mv app.js js/ 2>/dev/null
[ -f "movies.json" ] && mv movies.json data/ 2>/dev/null

# Verify required files
echo "Checking required files..."
required_files=(
    "index.html"
    "css/styles.css"
    "js/app.js"
    "js/help.js"
    "data/movies.json"
    "README.md"
    "HELP.md"
)

missing_files=0
for file in "${required_files[@]}"; do
    if [ ! -f "$file" ]; then
        echo "Warning: Missing $file"
        missing_files=$((missing_files + 1))
    fi
done

# Try to start the server on port 8000
echo "Starting web server..."
for port in {8000..8010}; do
    if ! lsof -i :$port > /dev/null 2>&1; then
        echo "Using port $port..."
        python3 -m http.server $port &
        SERVER_PID=$!
        
        # Wait a moment to ensure server starts
        sleep 2
        
        if ps -p $SERVER_PID > /dev/null; then
            echo ""
            echo "Setup complete!"
            echo "Access the demo at: http://localhost:$port"
            echo ""
            echo "Features available:"
            echo "- Browse and rent movies"
            echo "- Manage users"
            echo "- Track rentals"
            echo "- View statistics"
            echo "- Access help guide"
            echo ""
            echo "Press Ctrl+C to stop the server"
            
            # Wait for Ctrl+C
            trap 'echo "Stopping server..."; kill $SERVER_PID; exit 0' INT
            wait $SERVER_PID
            exit 0
        fi
    fi
done

echo "Error: Could not find an available port between 8000-8010"
exit 1
