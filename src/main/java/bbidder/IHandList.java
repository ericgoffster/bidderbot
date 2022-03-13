package bbidder;

/**
 * Represents a list of hands that have been created in the hand generator.
 * 
 * @author goffster
 *
 */
public interface IHandList {
    public int minTotalPoints(int suit);

    public int minHcp();

    public int maxHcp();

    public double avgLenInSuit(int suit);

    public int minInSuit(int suit);

    public int maxInSuit(int suit);
}
