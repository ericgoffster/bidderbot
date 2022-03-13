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
     * @param players 
     *            The current players
     * @param hand
     *            The hand
     * @return True if it matches the hand
     */
    public boolean matches(Players players, Hand hand);

    public InfSummary getSummary();

    IBoundInference negate();

    IBoundInference andWith(IBoundInference other);

    IBoundInference orWith(IBoundInference other);
}
