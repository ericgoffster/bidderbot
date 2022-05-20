package bbidder;

import java.util.stream.Stream;

/**
 * Represents a symbol that resolves to a strain.
 * @author goffster
 *
 */
public abstract class Symbol {
    /**
     * @return The resolved strain
     */
    public abstract int getResolvedStrain();

    /**
     * @param bid
     *            The bid to test
     * @return true if the given bid is compatible with the symbol
     */
    public abstract boolean compatibleWith(Bid bid);

    /**
     * 
     * @return true if the given bid should only be considered if the bid was not previously defined.
     */
    public abstract boolean isNonConvential();
    
    /**
     * 
     * @return true if the given bid should only be considered if the bid was not previously defined.
     */
    public abstract short getSeats();

    /**
     * Resolves the symbol into a stream of resolved symbols in the context of a suit table.
     * For example:
     *     If my symbol was "M", and the suitTable already had M=SPADE,
     *         then this would return a stream of a single item, with a resolved symbol of SPADE.
     *     If the suitTable was empty,
     *         then this would return a stream of two items,
     *              one with a resolved symbol of HEART (with a symbol table of M=HEART)
     *              the other with a resolved symbol of SPADE (with a symbol table of M=SPADE)
     * @param suitTable The suit table to lookup
     * @return Stream of context's
     */
    public abstract Stream<Context> resolveSuits(SuitTable suitTable);

    public final class Context {
        public final SuitTable suitTable;

        public Context(SuitTable suitTable) {
            super();
            this.suitTable = suitTable;
        }

        public Symbol getSymbol() {
            return Symbol.this;
        }
    }
}
