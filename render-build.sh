#!/usr/bin/env bash
# Render build script for Spring Boot application

set -e  # Exit on error

echo "Starting Render build..."

# Build the application with Maven
echo "Building with Maven..."
./mvnw clean package -DskipTests

echo "Build completed successfully!"
