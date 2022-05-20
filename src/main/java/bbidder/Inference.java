package bbidder;

import bbidder.utils.MyStream;

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
    public abstract MyStream<Context> resolveSuits(SuitTable suitTable);

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

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

}
