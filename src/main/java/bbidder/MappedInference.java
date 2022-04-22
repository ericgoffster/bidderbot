package bbidder;

public class MappedInference {
    public final IBoundInference inf;
    public final SuitSets ctx;

    public MappedInference(IBoundInference inf, SuitSets ctx) {
        super();
        this.inf = inf;
        this.ctx = ctx;
    }
}
