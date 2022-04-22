package bbidder;

import java.util.stream.Stream;

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
     * @param suitTable
     *            The symbol table
     * @return A list of inferences representing this inference bound to all possible
     *         symbols.
     */
    public abstract Stream<Context> resolveSymbols(SuitTable suitTable);

    public final class Context {
        public final SuitTable suitTable;

        public Context(SuitTable suitTable) {
            super();
            this.suitTable = suitTable;
        }

        public Inference getInference() {
            return Inference.this;
        }
    }
}
