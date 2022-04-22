package bbidder;

import java.util.List;

public abstract class Symbol {
    /**
     * @return The resolved value
     */
    public abstract int getResolved();
    
    /**
     * @param bid The bid to test
     * @return true if the given bid is compatible with the symbol
     */
    public abstract boolean compatibleWith(Bid bid);
    
    /**
     * 
     * @return true if the given bid should only be considered if the bid was not previously defined.
     */
    public abstract boolean nonConvential();
    
    public abstract List<SymbolContext> resolveSymbols(SymbolTable symbols);
    
    public final class SymbolContext {
        private final Symbol symbol;
        public final SymbolTable symbols;

        public SymbolContext(Symbol symbol, SymbolTable symbols) {
            super();
            this.symbol = symbol;
            this.symbols = symbols;
        }

        public Symbol getSymbol() {
            return symbol;
        }
    }
}
