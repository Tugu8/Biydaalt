package mn.num.flashcard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AppTest {
    @Test
    void organizePlacesRecentlyWrongCardsFirst() {
        Card first = new Card("Q1", "A1");
        Card second = new Card("Q2", "A2");
        Card third = new Card("Q3", "A3");
        Card fourth = new Card("Q4", "A4");

        second.recordAttempt(false);
        fourth.recordAttempt(false);

        List<Card> organized = new RecentMistakesFirstSorter().organize(List.of(first, second, third, fourth));

        assertEquals(List.of(second, fourth, first, third), organized);
    }

    @Test
    void organizeKeepsRelativeOrderWithinEachGroup() {
        Card firstWrong = new Card("Q1", "A1");
        Card secondWrong = new Card("Q2", "A2");
        Card firstCorrect = new Card("Q3", "A3");
        Card secondCorrect = new Card("Q4", "A4");

        firstWrong.recordAttempt(false);
        secondWrong.recordAttempt(false);

        List<Card> organized = new RecentMistakesFirstSorter()
                .organize(List.of(firstCorrect, firstWrong, secondCorrect, secondWrong));

        assertEquals(List.of(firstWrong, secondWrong, firstCorrect, secondCorrect), organized);
    }
}
