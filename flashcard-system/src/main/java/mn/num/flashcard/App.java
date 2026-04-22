package mn.num.flashcard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
    private static final String ORDER_ORIGINAL = "original";
    private static final String ORDER_RECENT_MISTAKES_FIRST = "recent-mistakes-first";

    public static void main(String[] args) {
        try {
            run(args);
        } catch (ParseException exception) {
            System.err.println("Error parsing command line arguments: " + exception.getMessage());
            printHelp(buildOptions());
            System.exit(1);
        } catch (IOException exception) {
            System.err.println("I/O error occurred: " + exception.getMessage());
            System.exit(1);
        }
    }

    static void run(String[] args) throws ParseException, IOException {
        Options options = buildOptions();
        CommandLine commandLine = new DefaultParser().parse(options, args);

        if (commandLine.hasOption("help")) {
            printHelp(options);
            return;
        }

        String[] positionalArgs = commandLine.getArgs();
        if (positionalArgs.length != 1) {
            throw new ParseException("A single flashcard file path is required.");
        }

        Path cardsFilePath = Path.of(positionalArgs[0]);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Flashcard System ===");
            System.out.println("1. Answer questions");
            System.out.println("2. Add questions");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextLine()) break;
            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                runQuiz(cardsFilePath, commandLine, scanner);
            } else if ("2".equals(choice)) {
                addQuestion(cardsFilePath, scanner);
            } else if ("3".equals(choice)) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Show help information.");
        options.addOption(Option.builder()
                .longOpt("order")
                .hasArg()
                .argName("original|recent-mistakes-first")
                .desc("Card order strategy. Defaults to original.")
                .build());
        options.addOption(Option.builder()
                .longOpt("repetitions")
                .hasArg()
                .argName("count")
                .desc("Number of rounds to run. Defaults to 1.")
                .build());
        options.addOption(Option.builder()
                .longOpt("invertCards")
                .desc("Ask with the answer side and expect the question side.")
                .build());
        options.addOption(Option.builder()
                .longOpt("showStats")
                .desc("Show statistics for each card at the end.")
                .build());
        return options;
    }

    private static void printHelp(Options options) {
        new HelpFormatter().printHelp("java -jar flashcard-system.jar [options] <cards-file>", options);
    }

    private static void validateOrder(String order) throws ParseException {
        if (!ORDER_ORIGINAL.equals(order) && !ORDER_RECENT_MISTAKES_FIRST.equals(order)) {
            throw new ParseException("Unsupported --order value: " + order);
        }
    }

    private static int parseRepetitions(String repetitionsValue) throws ParseException {
        try {
            int repetitions = Integer.parseInt(repetitionsValue);
            if (repetitions < 1) {
                throw new ParseException("--repetitions must be a positive integer.");
            }
            return repetitions;
        } catch (NumberFormatException exception) {
            throw new ParseException("--repetitions must be a positive integer.");
        }
    }

    private static CardOrganizer createOrganizer(String order) {
        if (ORDER_RECENT_MISTAKES_FIRST.equals(order)) {
            return new RecentMistakesFirstSorter();
        }
        return cards -> new ArrayList<>(cards);
    }

    static List<Card> loadCards(Path filePath) throws IOException {
        List<Card> cards = new ArrayList<>();

        for (String line : Files.readAllLines(filePath)) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }

            String[] parts = trimmedLine.split("\\|", 2);
            if (parts.length != 2) {
                throw new IOException("Invalid card format: " + line);
            }

            String question = parts[0].trim();
            String answer = parts[1].trim();
            cards.add(new Card(question, answer));
        }

        return cards;
    }

    private static void runRound(List<Card> cards, Scanner scanner, boolean invertCards, int round) {
        System.out.println("\nRound " + round);

        for (Card card : cards) {
            String prompt = invertCards ? card.getAnswer() : card.getQuestion();
            String expectedAnswer = invertCards ? card.getQuestion() : card.getAnswer();

            System.out.print(prompt + ": ");
            if (!scanner.hasNextLine()) return;
            String userAnswer = scanner.nextLine().trim();

            boolean isCorrect = userAnswer.equalsIgnoreCase(expectedAnswer.trim());
            card.recordAttempt(isCorrect);

            if (isCorrect) {
                System.out.println("Correct!");
                card.setWasWrongInLastRound(false);
            } else {
                System.out.println("Wrong!");
                card.setWasWrongInLastRound(true);
            }
        }
    }

    private static void printAchievements(List<Card> cards) {
        boolean lastRoundAllCorrect = cards.stream().noneMatch(Card::wasWrongInLastRound);

        if (lastRoundAllCorrect) {
            System.out.println("Congratulations! Achievement unlocked: CORRECT");
        }

        boolean hasRepeatCard = cards.stream().anyMatch(card -> card.getAttemptCount() > 5);
        if (hasRepeatCard) {
            System.out.println("Congratulations! Achievement unlocked: REPEAT");
        }

        boolean hasConfidentCard = cards.stream().anyMatch(card -> card.getCorrectCount() >= 3);
        if (hasConfidentCard) {
            System.out.println("Congratulations! Achievement unlocked: CONFIDENT");
        }

        boolean hasPerfectCard = cards.stream().anyMatch(card -> card.getAccuracy() == 100.0 && card.getAttemptCount() > 0);
        if (hasPerfectCard) {
            System.out.println("Congratulations! Achievement unlocked: PERFECT");
        }
    }

    private static void printStats(List<Card> cards) {
        System.out.println("\nCard Statistics:");
        for (Card card : cards) {
            System.out.printf("Question: %s | Attempts: %d | Correct: %d | Accuracy: %.1f%%\n",
                card.getQuestion(), card.getAttemptCount(), card.getCorrectCount(), card.getAccuracy());
        }
    }

    private static void runQuiz(Path cardsFilePath, CommandLine commandLine, Scanner scanner) throws IOException, ParseException {
        String order = commandLine.getOptionValue("order", ORDER_ORIGINAL);
        validateOrder(order);

        int repetitions = parseRepetitions(commandLine.getOptionValue("repetitions", "1"));
        boolean invertCards = commandLine.hasOption("invertCards");
        boolean showStats = commandLine.hasOption("showStats");

        List<Card> cards = loadCards(cardsFilePath);
        if (cards.isEmpty()) {
            System.out.println("No cards were loaded from the file.");
            return;
        }

        CardOrganizer organizer = createOrganizer(order);

        List<Card> lastRoundCards = new ArrayList<>(cards);

        for (int round = 1; round <= repetitions; round++) {
            lastRoundCards = organizer.organize(cards);
            runRound(lastRoundCards, scanner, invertCards, round);
        }

        if (ORDER_RECENT_MISTAKES_FIRST.equals(order)) {
            int extraRound = repetitions + 1;
            while (true) {
                // Collect wrong cards in reverse of last round's order so the most
                // recently answered wrong card appears first in the next round.
                List<Card> missedCards = new ArrayList<>();
                for (int i = lastRoundCards.size() - 1; i >= 0; i--) {
                    Card card = lastRoundCards.get(i);
                    if (card.wasWrongInLastRound()) {
                        missedCards.add(card);
                    }
                }

                if (missedCards.isEmpty()) {
                    System.out.println("\n\nБүх асуултыг зөв хариуллаа!");
                    break;
                }

                System.out.println("\n--- Буруу хариулсан картуудыг давтаж байна ---");
                List<Card> organizedMissed = organizer.organize(missedCards);
                runRound(organizedMissed, scanner, invertCards, extraRound++);
                lastRoundCards = organizedMissed;
            }
        }

        printAchievements(cards);

        if (showStats) {
            printStats(cards);
        }
    }

    private static void addQuestion(Path cardsFilePath, Scanner scanner) throws IOException {
        System.out.print("Enter your question: ");
        if (!scanner.hasNextLine()) return;
        String question = scanner.nextLine().trim();

        if (question.isEmpty()) {
            System.out.println("Question cannot be empty.");
            return;
        }

        System.out.print("Enter your answer: ");
        if (!scanner.hasNextLine()) return;
        String answer = scanner.nextLine().trim();

        if (answer.isEmpty()) {
            System.out.println("Answer cannot be empty.");
            return;
        }

        String cardLine = question + " | " + answer + "\n";
        Files.writeString(cardsFilePath, cardLine, StandardOpenOption.APPEND);

        System.out.println("Question added successfully!");
    }
}