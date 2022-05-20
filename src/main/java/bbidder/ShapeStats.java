package bbidder;

import java.util.Optional;

public class ShapeStats {
    final int suit;
    final Range range;
    final Optional<Double> avg;

    public ShapeStats(int suit, Range range, Optional<Double> avg) {
        super();
        this.suit = suit;
        this.range = range;
        this.avg = avg;
    }

    @Override
    public String toString() {
        if (!avg.isPresent()) {
            return "undefined " + Strain.getName(suit);
        }
        return range + " " + Strain.getName(suit) + " average " + avg;
    }
}