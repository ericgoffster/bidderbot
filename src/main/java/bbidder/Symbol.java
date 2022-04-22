package bbidder;

import java.util.stream.Stream;

public abstract class Symbol {
    /**
     * @return The resolved value
     */
    public abstract int getResolved();

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
    public abstract boolean nonConvential();

    public abstract Stream<Context> resolveSymbols(SuitTable suitTable);

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
