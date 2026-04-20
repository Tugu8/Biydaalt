# Flashcard System

A simple command-line flashcard application written in Java.

## Features

- Load flashcards from a text file
- Different ordering strategies (original, recent mistakes first)
- Multiple rounds
- Achievements system
- Statistics display

## Usage

Compile and run with Maven:

```bash
mvn clean compile
java -cp target/classes mn.num.flashcard.App cards.txt
```

## Options

- `--order`: Card order strategy (original|recent-mistakes-first)
- `--repetitions`: Number of rounds
- `--invertCards`: Swap question and answer
- `--showStats`: Display statistics at the end