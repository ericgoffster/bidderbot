package bbidder;

import java.util.Optional;

/**
 * Represents the statistics of a shape in a particular suit.
 * @author goffster
 *
 */
public class ShapeStats {
    public static final ShapeStats EMPTY = new ShapeStats(Range.none(13), Optional.empty());
    /**
     * The range of possible lengths.
     */
    public final Range lengths;
    
    /**
     * The average length.   Empty for "null" sets.
     */
    public final Optional<Double> averageLength;

    public ShapeStats(Range range, Optional<Double> avg) {
        super();
        this.lengths = range;
        this.averageLength = avg;
    }

    @Override
    public String toString() {
        if (!averageLength.isPresent()) {
            return "undefined";
        }
        return lengths + " " + " average " + averageLength.get();
    }
}