package loteria.model;

/**
 * An enumeration representing the categories of Lotería cards.
 * This helps in organizing and potentially applying special rules or logic
 * based on the type of card drawn.
 */
public enum CardCategory {
    /** Represents cards depicting people or human figures. */
    PEOPLE,

    /** Represents cards depicting animals. */
    ANIMALS,

    /** Represents cards depicting inanimate objects. */
    OBJECTS,

    /** Represents cards depicting elements of nature, like plants or celestial bodies. */
    NATURE,

    /** Represents cards depicting food items. */
    FOOD,

    /** Represents cards depicting actions or professions. */
    ACTIVITIES,

    /** Represents cards depicting abstract symbols or iconic items. */
    SYMBOLS,

    /** A general category for cards that do not fit neatly into the others. */
    MISC
}