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

    /**
     * Main entry point for the flashcard application.
     */
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

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== Flashcard System ===");
                System.out.println("1. Answer questions");
                System.out.println("2. Add questions");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

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
        }
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

    private static boolean runRound(List<Card> cards, Scanner scanner, boolean invertCards, int round) {
        boolean allCorrectWithoutMistake = true;
        System.out.println("Round " + round);

        for (Card card : cards) {
            String prompt = invertCards ? card.getAnswer() : card.getQuestion();
            String expectedAnswer = invertCards ? card.getQuestion() : card.getAnswer();

            boolean cardHadMistake = false;
            boolean isCorrect = false;
            card.setWasWrongInLastRound(false);
            int wrongAttempts = 0;

            while (!isCorrect) {
                System.out.print(prompt + ": ");
                String userAnswer = scanner.nextLine().trim();
                isCorrect = userAnswer.equalsIgnoreCase(expectedAnswer.trim());
                card.recordAttempt(isCorrect);

                if (isCorrect) {
                    System.out.println("Correct!");
                } else {
                    cardHadMistake = true;
                    wrongAttempts++;
                    System.out.println("Wrong. Try again.");
                    String hint = generateHint(expectedAnswer, wrongAttempts);
                    if (!hint.isEmpty()) {
                        System.out.println("Hint: " + hint);
                    }
                }
            }

            if (cardHadMistake) {
                allCorrectWithoutMistake = false;
            }

            card.setWasWrongInLastRound(cardHadMistake);
        }

        return allCorrectWithoutMistake;
    }

    private static String generateHint(String answer, int level) {
        if (level <= 0) return "";
        char[] chars = answer.toCharArray();
        StringBuilder hint = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i < level) {
                hint.append(chars[i]);
            } else {
                hint.append('_');
            }
        }
        return hint.toString();
    }

    private static void printAchievements(List<Card> cards, boolean lastRoundAllCorrectWithoutMistake) {
        if (lastRoundAllCorrectWithoutMistake) {
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
        boolean lastRoundAllCorrectWithoutMistake = false;

        for (int round = 1; round <= repetitions; round++) {
            List<Card> cardsForRound = organizer.organize(cards);
            lastRoundAllCorrectWithoutMistake = runRound(cardsForRound, scanner, invertCards, round);
        }

        printAchievements(cards, lastRoundAllCorrectWithoutMistake);

        if (showStats) {
            printStats(cards);
        }
    }

    private static void addQuestion(Path cardsFilePath, Scanner scanner) throws IOException {
        System.out.print("Enter your question: ");
        String question = scanner.nextLine().trim();

        if (question.isEmpty()) {
            System.out.println("Question cannot be empty.");
            return;
        }

        System.out.print("Enter your answer: ");
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
