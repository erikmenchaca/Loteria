package loteria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck of 54 Lotería cards.
 * This class is responsible for initializing, shuffling, and dealing the cards.
 */
public class Deck {

    /** The master list of all 54 unique cards. This list is never modified after creation. */
    private final List<LoteriaCard> cards;

    /** The current pile of cards that can still be drawn. */
    private List<LoteriaCard> remainingCards;

    /** A list of cards that have already been drawn from the deck in the current game. */
    private final List<LoteriaCard> calledCards;

    /**
     * Constructs a new Deck.
     * It initializes a full set of 54 Lotería cards and shuffles them.
     */
    public Deck() {
        this.cards = new ArrayList<>(54);
        this.calledCards = new ArrayList<>(54);
        this.remainingCards = new ArrayList<>(54);
        initializeDeck();
        reset();
    }

    /**
     * Populates the master 'cards' list from the LoteriaCards utility class.
     * This method is called once by the constructor.
     */
    private void initializeDeck() {
        // Assuming LoteriaCards.getAllCards() returns the 54 standard cards
        this.cards.addAll(LoteriaCards.getAllCards());
    }

    /**
     * Shuffles the list of remaining (undrawn) cards into a random order.
     */
    public void shuffle() {
        Collections.shuffle(this.remainingCards);
    }

    /**
     * Draws a single card from the top of the remaining cards pile.
     * The drawn card is moved from 'remainingCards' to 'calledCards'.
     *
     * @return The LoteriaCard drawn from the deck.
     * @throws IllegalStateException if the deck is empty.
     */
    public LoteriaCard drawCard() {
        if (!hasMoreCards()) {
            throw new IllegalStateException("The deck is empty. No cards to draw.");
        }
        // Removing from the end of an ArrayList is more efficient
        LoteriaCard drawnCard = remainingCards.remove(remainingCards.size() - 1);
        calledCards.add(drawnCard);
        return drawnCard;
    }

    /**
     * Checks if there are any cards left to draw.
     *
     * @return true if there are one or more cards remaining, false otherwise.
     */
    public boolean hasMoreCards() {
        return !remainingCards.isEmpty();
    }

    /**
     * Resets the deck for a new game.
     * All cards are returned to the 'remainingCards' pile, and the pile is re-shuffled.
     */
    public void reset() {
        calledCards.clear();
        remainingCards.clear();
        remainingCards.addAll(this.cards);
        shuffle();
    }

    /**
     * Retrieves a specific card by its number from the master list.
     *
     * @param cardNumber The number of the card to find (1-54).
     * @return The LoteriaCard with the specified number, or null if not found.
     */
    public LoteriaCard getCard(int cardNumber) {
        // Using a stream for a modern, functional approach
        return cards.stream()
                .filter(card -> card.getCardNumber() == cardNumber)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns an unmodifiable view of the master list of all 54 cards.
     *
     * @return A List containing all LoteriaCards.
     */
    public List<LoteriaCard> getAllCards() {
        return Collections.unmodifiableList(this.cards);
    }

    /**
     * Gets the number of cards left to be drawn.
     *
     * @return The count of remaining cards.
     */
    public int getRemainingCount() {
        return remainingCards.size();
    }
}