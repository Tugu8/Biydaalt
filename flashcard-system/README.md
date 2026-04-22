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

java -jar target/flashcard-system-1.0-SNAPSHOT.jar cards.txt

1. Maven ашиглах

mvn compile
mvn exec:java -Dexec.mainClass="mn.num.flashcard.App" -Dexec.args="cards.txt"

2. Java ажиллуулах

javac -d out src/main/java/mn/num/flashcard/\*.java
java -cp out mn.num.flashcard.App cards.txt

1. --help — Тусламж харуулах
   java -jar target/flashcard-system-1.0-SNAPSHOT.jar --help

2. --order — Картын дараалал
   Original (файлын дараалалаар):
   java -jar target/flashcard-system-1.0-SNAPSHOT.jar --order original cards.txt
   Буруу хариулсан картуудаа эхэнд:
   java -jar target/flashcard-system-1.0-SNAPSHOT.jar --order recent-mistakes-first cards.txt

3. --repetitions — Хэдэн раунд давтах
   java -jar target/flashcard-system-1.0-SNAPSHOT.jar --repetitions 3 cards.txt

4. --invertCards — Асуулт/хариултыг урвуулах
   java -jar target/flashcard-system-1.0-SNAPSHOT.jar --invertCards cards.txt

5. --showStats — Статистик харуулах
   java -jar target/flashcard-system-1.0-SNAPSHOT.jar --showStats cards.txt
