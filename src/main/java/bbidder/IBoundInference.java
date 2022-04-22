package bbidder;

import java.util.function.Predicate;

/**
 * Represents a an inference bound to a bidding sequence
 * and in context to likely hands from all players.
 * 
 * @author goffster
 *
 */
public interface IBoundInference extends Predicate<Hand> {
    /**
     * @return A summary of the inference
     */
    InfSummary getSummary();

    /**
     * @return A negation of the inference
     */
    IBoundInference negate();

    /**
     * @param other The other inference
     * @return An inference representing this & other.  Null if not possible.
     */
    IBoundInference andWith(IBoundInference other);

    /**
     * @param other The other inference
     * @return An inference representing this | other.  Null if not possible.
     */
    IBoundInference orWith(IBoundInference other);
}
