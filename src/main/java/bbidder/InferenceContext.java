package bbidder;

public final class InferenceContext {
    public final Inference inference;

    public final SymbolTable suits;

    public InferenceContext(Inference inference, SymbolTable suits) {
        super();
        this.inference = inference;
        this.suits = suits;
    }
}