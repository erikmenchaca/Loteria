package loteria.model;

/**
 * A custom exception for handling application-specific errors within the Lotería game.
 * This is a checked exception, meaning that methods that throw it must declare it,
 * and callers must handle it.
 */
public class LoteriaException extends Exception {

    /**
     * Constructs a new LoteriaException with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for
     * later retrieval by the {@link #getMessage()} method.
     */
    public LoteriaException(String message) {
        super(message);
    }

    /**
     * Constructs a new LoteriaException with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated in this exception's detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     * by the {@link #getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the
     * {@link #getCause()} method). (A {@code null} value is
     * permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public LoteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}