package bbidder;

import java.util.stream.Stream;

/**
 * Represents a symbol representing a strain.
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
