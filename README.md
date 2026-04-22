## Командын опционүүд (Монголоор)

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
