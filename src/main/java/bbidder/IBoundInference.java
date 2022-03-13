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
     * @param hand
     *            The hand
     * @return True if it matches the hand
     */
    public boolean matches(Hand hand);

    /**
     * @return The negated inference.
     */
    public IBoundInference negate();

    public IBoundInference andReduce(IBoundInference i);

    public IBoundInference orReduce(IBoundInference i);
}
