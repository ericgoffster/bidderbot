package bbidder;

import java.util.stream.Stream;

/**
 * Represents a dynamic set of suits.
 * 
 * @author goffster
 *
 */
public abstract class SuitSet {
    /**
     * @param players
     *            The set of players.
     * @return A bit pattern of suits.
     */
    public abstract short evaluate(Players players);

    /**
     * Resolves to a stream of suitsets with suits bounds to symbols.
     * 
     * @param suitTable
     *            The suit table
     * @return A Stream of suitset,symbol table pairs.
     */
    public abstract Stream<Context> resolveSuits(SuitTable suitTable);
    
    public final class Context {
        public final SuitTable suitTable;

        public Context(SuitTable suitTable) {
            super();
            this.suitTable = suitTable;
        }

        public SuitSet getSuitSet() {
            return SuitSet.this;
        }
    }
}