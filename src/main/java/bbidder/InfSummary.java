package bbidder;

public class InfSummary {
    public final ShapeSet shape;
    public final Range tpts;
    
    public InfSummary(ShapeSet shape, Range tpts) {
        super();
        this.shape = shape;
        this.tpts = tpts;
    }

    public static final InfSummary ALL = new InfSummary(ShapeSet.ALL, Range.all(40));
    public static final InfSummary NONE = new InfSummary(ShapeSet.NONE, Range.none(40));
    
    public InfSummary and(InfSummary other) {
        return new InfSummary(shape.and(other.shape), tpts.and(other.tpts));
    }
    
    public InfSummary or(InfSummary other) {
        return new InfSummary(shape.or(other.shape), tpts.or(other.tpts));
    }

    @Override
    public String toString() {
        return shape + " , tpts=" + tpts;
    }
    
    public boolean isEmpty() {
        return shape.isEmpty() || tpts.isEmpty();
    }
    
    public int minInSuit(int suit) {
        return shape.minInSuit(suit);
    }

    public int minTotalPts() {
        return tpts.lowest();
    }

    public double avgLenInSuit(int suit) {
        return shape.avgLenInSuit(suit);
    }
}
