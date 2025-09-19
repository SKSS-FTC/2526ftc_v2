#!/bin/bash

# Create the bin directory if it doesn't exist
mkdir -p bin

# Find all .java files in the src directory and build them into .class files
echo "Compiling Java source files..."
javac -d bin $(find src -name "*.java")

# Check if the compilation succeeded
if [ $? -eq 0 ]; then
    echo "Compilation successful. Class files stored in 'bin/'."
else
    echo "Compilation failed. Check the source code for errors."
    exit 1
fi

# Run the program after building
echo "Running the program..."
java -cp bin Main
