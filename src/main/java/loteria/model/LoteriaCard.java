package loteria.model;

import java.util.Objects;

/**
 * Represents a single, immutable Lotería card.
 * Each card has a unique number, a name, a riddle, and belongs to a category.
 * This class properly overrides equals() and hashCode() based on the unique card number.
 */
public class LoteriaCard {

    private final int cardNumber;
    private final String name;
    private final String spanishName;
    private final String riddle;
    private final CardCategory category;

    /**
     * Constructs a new LoteriaCard.
     *
     * @param cardNumber   The unique number of the card (1-54).
     * @param name         The English name of the card.
     * @param spanishName  The Spanish name of the card.
     * @param riddle       The traditional riddle or saying for the card.
     * @param category     The category the card belongs to.
     */
    public LoteriaCard(int cardNumber, String name, String spanishName, String riddle, CardCategory category) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.spanishName = spanishName;
        this.riddle = riddle;
        this.category = category;
    }

    /**
     * @return The card's official number.
     */
    public int getCardNumber() {
        return cardNumber;
    }

    /**
     * @return The card's name in English.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The card's name in Spanish.
     */
    public String getSpanishName() {
        return spanishName;
    }

    /**
     * @return The riddle or phrase associated with the card.
     */
    public String getRiddle() {
        return riddle;
    }

    /**
     * @return The CardCategory of the card.
     */
    public CardCategory getCategory() {
        return category;
    }

    /**
     * Returns a user-friendly string representation of the card.
     * Example: "#1: El Gallo (The Rooster)"
     *
     * @return A formatted string for display.
     */
    @Override
    public String toString() {
        return "#" + cardNumber + ": " + spanishName + " (" + name + ")";
    }

    /**
     * Compares this card to another object for equality.
     * Two LoteriaCard objects are considered equal if they have the same card number.
     *
     * @param obj The object to compare against.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoteriaCard that = (LoteriaCard) obj;
        return cardNumber == that.cardNumber;
    }

    /**
     * Generates a hash code for the LoteriaCard.
     * The hash code is based solely on the unique card number.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardNumber);
    }
}