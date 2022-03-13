package bbidder;

import java.util.Objects;

public class InfSummary {
    public static final InfSummary ALL = new InfSummary(ShapeSet.ALL, Range.all(40), StopperSet.ALL);
    public static final InfSummary NONE = new InfSummary(ShapeSet.NONE, Range.none(40), StopperSet.NONE);

    public final ShapeSet shape;
    public final Range tpts;
    public final StopperSet stoppers;

    public InfSummary(ShapeSet shape, Range tpts, StopperSet stoppers) {
        super();
        this.shape = shape;
        this.tpts = tpts;
        this.stoppers = stoppers;
    }

    public InfSummary and(InfSummary other) {
        return new InfSummary(shape.and(other.shape), tpts.and(other.tpts), stoppers.and(other.stoppers));
    }

    public InfSummary or(InfSummary other) {
        return new InfSummary(shape.or(other.shape), tpts.or(other.tpts), stoppers.or(other.stoppers));
    }

    @Override
    public String toString() {
        return shape + "," + tpts + " tpts" + "," + stoppers;
    }

    public boolean isEmpty() {
        return shape.isEmpty() || tpts.isEmpty();
    }

    public Range getSuit(int suit) {
        return shape.getSuit(suit);
    }

    public int minTotalPts() {
        return tpts.lowest();
    }

    public double avgLenInSuit(int suit) {
        return shape.avgLenInSuit(suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shape, stoppers, tpts);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InfSummary other = (InfSummary) obj;
        return Objects.equals(shape, other.shape) && Objects.equals(stoppers, other.stoppers) && Objects.equals(tpts, other.tpts);
    }
}
