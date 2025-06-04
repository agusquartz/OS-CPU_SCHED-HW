
# Makefile

SRC_DIR := src
BIN_DIR := bin
MAIN_CLASS := Main

# Find all .java source files in the src directory
SOURCES := $(shell find $(SRC_DIR) -name '*.java')

# Default target: compile all sources
all:
	@mkdir -p $(BIN_DIR)
	javac $(SOURCES) -d $(BIN_DIR)

# Run the program
run: all
	java -cp $(BIN_DIR) $(MAIN_CLASS)

# Clean build files
clean:
	rm -rf $(BIN_DIR)/*
