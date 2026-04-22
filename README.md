# Flashcard System

A simple command-line flashcard application written in Java.

## Features

- Load flashcards from a text file
- Different ordering strategies (original, recent mistakes first)
- Multiple rounds
- Achievements system
- Statistics display

## Usage

### Running the Application

#### 1. Using Maven

```bash
mvn compile
mvn exec:java -Dexec.mainClass="mn.num.flashcard.App" -Dexec.args="cards.txt"
```

#### 2. Using Java Directly

```bash
javac -d out src/main/java/mn/num/flashcard/*.java
java -cp out mn.num.flashcard.App cards.txt
```

#### 3. Using JAR File

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar cards.txt
```

## Options

- `--order`: Card order strategy (original|recent-mistakes-first)
- `--repetitions`: Number of rounds
- `--invertCards`: Swap question and answer
- `--showStats`: Display statistics at the end

## Example Usage

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar cards.txt --order recent-mistakes-first --repetitions 3 --showStats
```
##Командын опционүүд 

1. **--help** — Тусламж харуулах
   ```bash
   java -jar flashcard-system.jar --help
   ```

2. **--order** — Картын дараалал
   - Original (файлын дараалалаар):
     ```bash
     java -jar flashcard-system.jar --order original cards.txt
     ```
   - Буруу хариулсан картуудаа эхэнд:
     ```bash
     java -jar flashcard-system.jar --order recent-mistakes-first cards.txt
     ```

3. **--repetitions** — Хэдэн раунд давтах
   ```bash
   java -jar flashcard-system.jar --repetitions 3 cards.txt
   ```

4. **--invertCards** — Асуулт/хариултыг урвуулах
   ```bash
   java -jar flashcard-system.jar --invertCards cards.txt
   ```

5. **--showStats** — Статистик харуулах
   ```bash
   java -jar flashcard-system.jar --showStats cards.txt
   ```
