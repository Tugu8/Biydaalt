package mn.num.flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sorts cards to prioritize those that were wrong in the last round, with most recent mistakes first.
 */
public class RecentMistakesFirstSorter implements CardOrganizer {
    @Override
    public List<Card> organize(List<Card> cards) {
        List<Card> wrongCards = new ArrayList<>();
        List<Card> correctCards = new ArrayList<>();

        for (Card card : cards) {
            if (card.wasWrongInLastRound()) {
                wrongCards.add(card);
            } else {
                correctCards.add(card);
            }
        }

        wrongCards.addAll(correctCards);
        return wrongCards;
    }
}
