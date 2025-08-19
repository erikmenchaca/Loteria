package loteria.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the "Caller" (El Gritón) in a game of Lotería.
 * This class is responsible for drawing cards from the deck and announcing them.
 */
public class Caller {

    /** The deck of cards from which the caller will draw. */
    private final Deck deck;

    /** A list of classic phrases the caller might use before announcing the card. */
    private final List<String> callingPhrases;

    /** A random number generator for selecting a calling phrase. */
    private final Random random;

    /**
     * Constructs a new Caller with a specific deck of cards.
     *
     * @param deck The game deck to be used. Must not be null.
     */
    public Caller(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null.");
        }
        this.deck = deck;
        this.random = new Random();
        this.callingPhrases = new ArrayList<>();
        initializePhrases();
    }

    /**
     * Draws the next card from the deck.
     *
     * @return The LoteriaCard that was drawn.
     * @throws IllegalStateException if there are no more cards in the deck to draw.
     */
    public LoteriaCard callCard() {
        if (!deck.hasMoreCards()) {
            throw new IllegalStateException("No more cards in the deck to call.");
        }
        return deck.drawCard();
    }

    /**
     * Creates a fun, randomized announcement string for a given card.
     * Example: "¡Corre y se va con... El Gallo!"
     *
     * @param card The card to announce.
     * @return A formatted string announcing the card.
     */
    public String announceCard(LoteriaCard card) {
        if (callingPhrases.isEmpty()) {
            return card.getName() + "!";
        }
        String phrase = callingPhrases.get(random.nextInt(callingPhrases.size()));
        return phrase + " " + card.getName() + "!";
    }

    /**
     * Gets the traditional riddle or phrase associated with a specific card.
     * This is retrieved directly from the LoteriaCard object.
     *
     * @param card The card whose riddle is requested.
     * @return The riddle string from the card.
     */
    public String getCallingPhrase(LoteriaCard card) {
        return card.getRiddle();
    }

    /**
     * Checks if there are more cards left in the deck to be called.
     *
     * @return true if the deck has more cards, false otherwise.
     */
    public boolean hasMoreCards() {
        return deck.hasMoreCards();
    }

    /**
     * Helper method to populate the list of introductory calling phrases.
     */
    private void initializePhrases() {
        callingPhrases.add("¡Corre y se va con...!");
        callingPhrases.add("¡Se va y se corre con...!");
        callingPhrases.add("¡Siguiente carta...!");
        callingPhrases.add("¡La que sigue es...!");
        callingPhrases.add("¡Atención, atención...!");
    }
}