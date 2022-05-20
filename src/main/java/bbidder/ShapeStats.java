package bbidder;

import java.util.OptionalDouble;

/**
 * Represents the statistics of a shape in a particular suit.
 * @author goffster
 *
 */
public class ShapeStats {
    public static final ShapeStats EMPTY = new ShapeStats(SuitLengthRange.NONE, OptionalDouble.empty());
    /**
     * The range of possible lengths.
     */
    public final SuitLengthRange lengths;
    
    /**
     * The average length.   Empty for "null" sets.
     */
    public final OptionalDouble averageLength;

    public ShapeStats(SuitLengthRange range, OptionalDouble avg) {
        super();
        this.lengths = range;
        this.averageLength = avg;
    }

    @Override
    public String toString() {
        if (!averageLength.isPresent()) {
            return "undefined";
        }
        return lengths + " " + " average " + averageLength.getAsDouble();
    }
}