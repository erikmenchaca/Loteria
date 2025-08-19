package loteria.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A utility class that acts as a factory and repository for the 54 standard Lotería cards.
 * This class cannot be instantiated. All cards are created once and stored statically.
 */
public final class LoteriaCards {

    // A map for efficient lookup of cards by their number.
    private static final Map<Integer, LoteriaCard> CARD_MAP;

    // --- Public static final constants for easy access to a few well-known cards ---
    public static final LoteriaCard EL_GALLO;
    public static final LoteriaCard EL_DIABLO;
    public static final LoteriaCard LA_DAMA;
    public static final LoteriaCard EL_CATRIN;
    public static final LoteriaCard LA_SIRENA;
    public static final LoteriaCard LA_CALAVERA;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private LoteriaCards() {}

    /**
     * Static initializer block. This code runs only once when the class is first loaded,
     * creating all 54 card objects and populating the lookup map.
     */
    static {
        // Create a stream of all card objects
        Stream<LoteriaCard> cardStream = Stream.of(
            new LoteriaCard(1, "The Rooster", "El Gallo", "El que le cantó a San Pedro.", CardCategory.ANIMALS),
            new LoteriaCard(2, "The Devil", "El Diablo", "Pórtate bien cuatito, si no te lleva el coloradito.", CardCategory.SYMBOLS),
            new LoteriaCard(3, "The Lady", "La Dama", "Puliendo el paso, por toda la calle real.", CardCategory.PEOPLE),
            new LoteriaCard(4, "The Dandy", "El Catrín", "Don Ferruco en la alameda, su bastón quería tirar.", CardCategory.PEOPLE),
            new LoteriaCard(5, "The Umbrella", "El Paraguas", "Para el sol y para el agua.", CardCategory.OBJECTS),
            new LoteriaCard(6, "The Mermaid", "La Sirena", "Con los cantos de sirena, no te vayas a marear.", CardCategory.NATURE),
            new LoteriaCard(7, "The Ladder", "La Escalera", "Súbeme paso a pasito, no quieras pegar brinquitos.", CardCategory.OBJECTS),
            new LoteriaCard(8, "The Bottle", "La Botella", "La herramienta del borracho.", CardCategory.OBJECTS),
            new LoteriaCard(9, "The Barrel", "El Barril", "Tanto bebió el albañil, que quedó como barril.", CardCategory.OBJECTS),
            new LoteriaCard(10, "The Tree", "El Árbol", "El que a buen árbol se arrima, buena sombra le cobija.", CardCategory.NATURE),
            new LoteriaCard(11, "The Melon", "El Melón", "Me lo das o me lo quitas.", CardCategory.FOOD),
            new LoteriaCard(12, "The Valient One", "El Valiente", "Por qué le corres cobarde, trayendo tan buen puñal.", CardCategory.PEOPLE),
            new LoteriaCard(13, "The Little Hat", "El Gorrito", "Ponle su gorrito al nene, no se nos vaya a resfriar.", CardCategory.OBJECTS),
            new LoteriaCard(14, "Death", "La Muerte", "La muerte siriqui siaca.", CardCategory.SYMBOLS),
            new LoteriaCard(15, "The Pear", "La Pera", "El que espera, desespera.", CardCategory.FOOD),
            new LoteriaCard(16, "The Flag", "La Bandera", "Verde, blanco y colorado, la bandera del soldado.", CardCategory.SYMBOLS),
            new LoteriaCard(17, "The Bandolon", "El Bandolón", "Tocando su bandolón, está el mariachi Simón.", CardCategory.OBJECTS),
            new LoteriaCard(18, "The Cello", "El Violoncello", "Creció tanto el violoncello, que ya no cupo en el cielo.", CardCategory.OBJECTS),
            new LoteriaCard(19, "The Heron", "La Garza", "Al otro lado del río, tengo mi banco de arena.", CardCategory.ANIMALS),
            new LoteriaCard(20, "The Bird", "El Pájaro", "Tú me traes a puros brincos, como pájaro en la rama.", CardCategory.ANIMALS),
            new LoteriaCard(21, "The Hand", "La Mano", "La mano de un criminal.", CardCategory.SYMBOLS),
            new LoteriaCard(22, "The Boot", "La Bota", "Una bota igual que la otra.", CardCategory.OBJECTS),
            new LoteriaCard(23, "The Moon", "La Luna", "El farol de los enamorados.", CardCategory.NATURE),
            new LoteriaCard(24, "The Parrot", "El Cotorro", "Cotorro, cotorro, saca la pata y empiézame a platicar.", CardCategory.ANIMALS),
            new LoteriaCard(25, "The Drunk", "El Borracho", "A qué borracho tan necio, ya no lo puedo aguantar.", CardCategory.PEOPLE),
            new LoteriaCard(26, "The Negro", "El Negrito", "El que se comió el azúcar.", CardCategory.PEOPLE),
            new LoteriaCard(27, "The Heart", "El Corazón", "No me extrañes corazón, que regreso en el camión.", CardCategory.SYMBOLS),
            new LoteriaCard(28, "The Watermelon", "La Sandía", "La barriga que Juan tenía, era de pura sandía.", CardCategory.FOOD),
            new LoteriaCard(29, "The Drum", "El Tambor", "No te arrugues, cuero viejo, que te quiero pa' tambor.", CardCategory.OBJECTS),
            new LoteriaCard(30, "The Shrimp", "El Camarón", "Camarón que se duerme, se lo lleva la corriente.", CardCategory.ANIMALS),
            new LoteriaCard(31, "The Arrows", "Las Jaras", "Las jaras del indio Adán, donde pegan, dan.", CardCategory.OBJECTS),
            new LoteriaCard(32, "The Musician", "El Músico", "El músico trompas de hule, ya no me quiere tocar.", CardCategory.PEOPLE),
            new LoteriaCard(33, "The Spider", "La Araña", "Atarántamela a palos, no me la dejes llegar.", CardCategory.ANIMALS),
            new LoteriaCard(34, "The Soldier", "El Soldado", "Uno, dos y tres, el soldado pa'l cuartel.", CardCategory.PEOPLE),
            new LoteriaCard(35, "The Star", "La Estrella", "La guía de los marineros.", CardCategory.NATURE),
            new LoteriaCard(36, "The Saucepan", "El Cazo", "El caso que te hago es poco.", CardCategory.OBJECTS),
            new LoteriaCard(37, "The World", "El Mundo", "Este mundo es una bola, y nosotros un bolón.", CardCategory.SYMBOLS),
            new LoteriaCard(38, "The Apache", "El Apache", "¡Ah, Chihuahua! Cuánto apache con pantalón y huarache.", CardCategory.PEOPLE),
            new LoteriaCard(39, "The Nopal", "El Nopal", "Al nopal lo van a ver, nomás cuando tiene tunas.", CardCategory.NATURE),
            new LoteriaCard(40, "The Scorpion", "El Alacrán", "El que con la cola pica, le dan una paliza.", CardCategory.ANIMALS),
            new LoteriaCard(41, "The Rose", "La Rosa", "Rosita, Rosaura, ven que te quiero ahora.", CardCategory.NATURE),
            new LoteriaCard(42, "The Skull", "La Calavera", "Al pasar por el panteón, me encontré un calaverón.", CardCategory.SYMBOLS),
            new LoteriaCard(43, "The Bell", "La Campana", "Tú con la campana y yo con tu hermana.", CardCategory.OBJECTS),
            new LoteriaCard(44, "The Jug", "El Cantarito", "Tanto va el cántaro al agua, que se quiebra y te moja.", CardCategory.OBJECTS),
            new LoteriaCard(45, "The Deer", "El Venado", "Saltando va buscando, pero no ve nada.", CardCategory.ANIMALS),
            new LoteriaCard(46, "The Sun", "El Sol", "La cobija de los pobres.", CardCategory.NATURE),
            new LoteriaCard(47, "The Crown", "La Corona", "El sombrero de los reyes.", CardCategory.SYMBOLS),
            new LoteriaCard(48, "The Canoe", "La Chalupa", "Rema y rema va Lupita, sentada en su chalupita.", CardCategory.OBJECTS),
            new LoteriaCard(49, "The Pine Tree", "El Pino", "Fresco y oloroso, en todo tiempo hermoso.", CardCategory.NATURE),
            new LoteriaCard(50, "The Fish", "El Pescado", "El que por la boca muere, aunque mudo fuere.", CardCategory.ANIMALS),
            new LoteriaCard(51, "The Palm Tree", "La Palma", "Palmero, sube a la palma y bájame un coco real.", CardCategory.NATURE),
            new LoteriaCard(52, "The Flowerpot", "La Maceta", "El que nace pa' maceta, no sale del corredor.", CardCategory.OBJECTS),
            new LoteriaCard(53, "The Harp", "El Arpa", "Arpa vieja de mi suegra, ya no sirves pa' tocar.", CardCategory.OBJECTS),
            new LoteriaCard(54, "The Frog", "La Rana", "Al ver a la suegra, pegó un brinco de rana.", CardCategory.ANIMALS)
        );
        
        // Populate the map for quick lookups.
        CARD_MAP = cardStream.collect(Collectors.toUnmodifiableMap(LoteriaCard::getCardNumber, card -> card));

        // Initialize the public constants
        EL_GALLO = CARD_MAP.get(1);
        EL_DIABLO = CARD_MAP.get(2);
        LA_DAMA = CARD_MAP.get(3);
        EL_CATRIN = CARD_MAP.get(4);
        LA_SIRENA = CARD_MAP.get(6);
        LA_CALAVERA = CARD_MAP.get(42);
    }
    
    /**
     * Returns an unmodifiable list of all 54 Lotería cards.
     *
     * @return A List containing every LoteriaCard.
     */
    public static List<LoteriaCard> getAllCards() {
        return List.copyOf(CARD_MAP.values());
    }

    /**
     * Retrieves a specific card by its unique number.
     *
     * @param number The number of the card to retrieve (1-54).
     * @return The corresponding LoteriaCard, or null if the number is invalid.
     */
    public static LoteriaCard getCardByNumber(int number) {
        return CARD_MAP.get(number);
    }

    /**
     * Returns a list of all cards that belong to a specific category.
     *
     * @param category The category to filter by.
     * @return An unmodifiable List of cards in the specified category.
     */
    public static List<LoteriaCard> getCardsByCategory(CardCategory category) {
        return CARD_MAP.values().stream()
                .filter(card -> card.getCategory() == category)
                .collect(Collectors.toUnmodifiableList());
    }
}