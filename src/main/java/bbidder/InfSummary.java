package bbidder;

public class InfSummary {
    public final ShapeSet shape;
    public final Range hcp;
    public final Range ctpts;
    public final Range tpts;
    
    public InfSummary(ShapeSet shape, Range hcp, Range ctpts, Range tpts) {
        super();
        this.shape = shape;
        this.hcp = hcp;
        this.ctpts = ctpts;
        this.tpts = tpts;
    }

    public static final InfSummary ALL = new InfSummary(ShapeSet.ALL, Range.all(40), Range.all(40), Range.all(40));
    public static final InfSummary NONE = new InfSummary(ShapeSet.NONE, Range.none(40), Range.none(40), Range.none(40));
    
    public InfSummary and(InfSummary other) {
        return new InfSummary(shape.and(other.shape), hcp.and(other.hcp), ctpts.and(other.ctpts), tpts.and(other.tpts));
    }
    
    public InfSummary or(InfSummary other) {
        return new InfSummary(shape.or(other.shape), hcp.or(other.hcp), ctpts.or(other.ctpts), tpts.or(other.tpts));
    }

    @Override
    public String toString() {
        return shape + " , hcp=" + hcp + ",ctpts=" + ctpts + ",tpts=" + tpts;
    }
    
    public boolean isEmpty() {
        return shape.isEmpty() || hcp.isEmpty() || ctpts.isEmpty() || tpts.isEmpty();
    }
}
