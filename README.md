# Flashcard System

A simple command-line flashcard application written in Java.

## Features

- Load flashcards from a text file
- Different ordering strategies (original, recent mistakes first)
- Multiple rounds
- Achievements system
- Statistics display

## Build & Run

```bash
mvn clean package -DskipTests
java -jar target/flashcard-system-1.0-SNAPSHOT.jar [options] cards.txt
```

## Main Menu

```
=== Flashcard System ===
1. Answer questions
2. Add questions
3. Exit
Enter your choice:
```

---

## Options

### `--help` — Тусламж харуулах

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar --help cards.txt
```

```
usage: java -jar flashcard-system.jar [options] <cards-file>
 -h,--help                                     Show help information.
    --invertCards                              Ask with the answer side
                                               and expect the question
                                               side.
    --order <original|recent-mistakes-first>   Card order strategy.
                                               Defaults to original.
    --repetitions <count>                      Number of rounds to run.
                                               Defaults to 1.
    --showStats                                Show statistics for each
                                               card at the end.
```

---

### Default — Асуулт хариулах (зөв хариулсан үед)

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar cards.txt
```

```
Round 1
What is Java?: Correct!
2 * 2: Correct!
Capital of Mongolia: Correct!
what is my name: Correct!
Congratulations! Achievement unlocked: CORRECT
Congratulations! Achievement unlocked: PERFECT
```

---

### Default — Асуулт хариулах (буруу хариулсан үед)

```
Round 1
What is Java?: Wrong!
2 * 2: Wrong!
Capital of Mongolia: Wrong!
what is my name: Wrong!
```

---

### `--order original` — Файлын дараалалаар

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar --order original cards.txt
```

```
Round 1
What is Java?: Correct!
2 * 2: Correct!
Capital of Mongolia: Correct!
what is my name: Correct!
Congratulations! Achievement unlocked: CORRECT
Congratulations! Achievement unlocked: PERFECT
```

---

### `--order recent-mistakes-first` — Буруу хариулсан картуудыг эхэнд

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar --order recent-mistakes-first cards.txt
```

```
Round 1
What is Java?: Wrong!
2 * 2: Wrong!
Capital of Mongolia: Correct!
what is my name: Correct!

--- Буруу хариулсан картуудыг давтаж байна ---

Round 2
2 * 2: Correct!
What is Java?: Correct!

Бүх асуултыг зөв хариуллаа!
Congratulations! Achievement unlocked: CORRECT
Congratulations! Achievement unlocked: PERFECT
```

---

### `--repetitions N` — Хэдэн раунд давтах

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar --repetitions 2 cards.txt
```

```
Round 1
What is Java?: Correct!
2 * 2: Correct!
Capital of Mongolia: Correct!
what is my name: Correct!

Round 2
What is Java?: Correct!
2 * 2: Correct!
Capital of Mongolia: Correct!
what is my name: Correct!
Congratulations! Achievement unlocked: CORRECT
Congratulations! Achievement unlocked: PERFECT
```

---

### `--invertCards` — Асуулт/хариултыг урвуулах

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar --invertCards cards.txt
```

```
Round 1
A programming language: Correct!
4: Correct!
Ulaanbaatar: Correct!
Tugu: Correct!
Congratulations! Achievement unlocked: CORRECT
Congratulations! Achievement unlocked: PERFECT
```

---

### `--showStats` — Статистик харуулах

```bash
java -jar target/flashcard-system-1.0-SNAPSHOT.jar --showStats cards.txt
```

```
Round 1
What is Java?: Correct!
2 * 2: Correct!
Capital of Mongolia: Correct!
what is my name: Correct!
Congratulations! Achievement unlocked: CORRECT
Congratulations! Achievement unlocked: PERFECT

Card Statistics:
Question: What is Java? | Attempts: 1 | Correct: 1 | Accuracy: 100.0%
Question: 2 * 2 | Attempts: 1 | Correct: 1 | Accuracy: 100.0%
Question: Capital of Mongolia | Attempts: 1 | Correct: 1 | Accuracy: 100.0%
Question: what is my name | Attempts: 1 | Correct: 1 | Accuracy: 100.0%
```

---

## Achievements

| Achievement | Нөхцөл |
|-------------|--------|
| `CORRECT`   | Сүүлийн раундад бүх асуултыг зөв хариулсан |
| `PERFECT`   | Ямар нэг картыг 100% зөв хариулсан |
| `CONFIDENT` | Ямар нэг картыг 3 ба түүнээс дээш удаа зөв хариулсан |
| `REPEAT`    | Ямар нэг картыг 5-аас дээш удаа оролдсон |

---

## Асуулт нэмэх (menu option 2)

```
Enter your question: What is 2+2?
Enter your answer: 4
Question added successfully!
```

---

## Буруу сонголт хийсэн үед

```
Invalid choice. Please enter 1, 2, or 3.
```

---

## cards.txt формат

```
Question | Answer
What is Java? | A programming language
2 * 2 | 4
Capital of Mongolia | Ulaanbaatar
```
