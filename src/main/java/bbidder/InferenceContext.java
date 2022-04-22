package bbidder;

/**
 * Represents a bound inference in the context of a symbol table.
 * @author goffster
 *
 */
public final class InferenceContext {
    public final Inference inference;
    public final SymbolTable symbols;

    public InferenceContext(Inference inference, SymbolTable symbols) {
        super();
        this.inference = inference;
        this.symbols = symbols;
    }
}