package bbidder;

/**
 * Represents a an inference bound to a bidding sequence
 * and in context to likely hands from all players.
 * 
 * @author goffster
 *
 */
public interface IBoundInference  {
    /**
     * @param hand
     *            The hand
     * @return True if it matches the hand
     */
    public boolean matches(Hand hand);
    
    public InfSummary getSummary();
    public InfSummary getNotSummary();
    
    IBoundInference andWith(InfSummary summary);
    IBoundInference negate();
}
