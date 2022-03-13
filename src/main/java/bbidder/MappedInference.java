package bbidder;

public class MappedInference {
    public final IBoundInference inf;
    public final InferenceContext ctx;
    public MappedInference(IBoundInference inf, InferenceContext ctx) {
        super();
        this.inf = inf;
        this.ctx = ctx;
    }
}
