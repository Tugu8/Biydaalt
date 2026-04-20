package mn.num.flashcard;

import java.util.List;

/**
 * Interface for organizing cards in different orders.
 */
public interface CardOrganizer {
    List<Card> organize(List<Card> cards);
}