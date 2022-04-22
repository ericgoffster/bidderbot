package bbidder;

import java.util.List;

/**
 * A Bridge inference.
 */
public abstract class Inference {
    /**
     * @param players
     *            Inferences for all players to date.
     * @return An inference where all entities based on external influences have been resolved.
     */
    public abstract IBoundInference bind(Players players);

    /**
     * 
     * @param symbols
     *            The symbol table
     * @return A list of inferences representing this inference bound to all possible
     *         symbols.
     */
    public abstract List<InferenceContext> resolveSymbols(SymbolTable symbols);
}
