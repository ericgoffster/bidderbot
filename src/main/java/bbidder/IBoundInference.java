package bbidder;

/**
 * Represents a an inference bound to a bidding sequence
 * and in context to likely hands from all players.
 * 
 * @author goffster
 *
 */
public interface IBoundInference {
    /**
     * @param hand The hand
     * @return True if it matches the hand 
     */
    public boolean matches(Hand hand);

    /**
     * @return True if the inference is naturally negatable.
     */
    public boolean negatable();

    /**
     * @return The negated inference.
     */
    public IBoundInference negate();
}
