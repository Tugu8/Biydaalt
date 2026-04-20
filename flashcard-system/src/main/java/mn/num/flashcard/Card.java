package mn.num.flashcard;

/**
 * Represents a flashcard with a question and answer.
 */
public class Card {
    private final String question;
    private final String answer;
    private boolean wasWrongInLastRound = false;
    private int attemptCount = 0;
    private int correctCount = 0;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public void recordAttempt(boolean isCorrect) {
        attemptCount++;
        if (isCorrect) {
            correctCount++;
            wasWrongInLastRound = false;
        } else {
            wasWrongInLastRound = true;
        }
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean wasWrongInLastRound() {
        return wasWrongInLastRound;
    }

    public void setWasWrongInLastRound(boolean wasWrongInLastRound) {
        this.wasWrongInLastRound = wasWrongInLastRound;
    }

    public boolean isWasWrongInLastRound() {
        return wasWrongInLastRound;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    /**
     * Calculates the accuracy of attempts for this card.
     * @return accuracy as a percentage (0-100), or 0 if no attempts.
     */
    public double getAccuracy() {
        if (attemptCount == 0) {
            return 0.0;
        }
        return (double) correctCount / attemptCount * 100.0;
    }
}
