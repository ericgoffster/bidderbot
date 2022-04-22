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
    
    public abstract List<Context> resolveSymbols(SymbolTable symbols);
    
    public final class Context {
        public final SymbolTable symbols;

        public Context(SymbolTable symbols) {
            super();
            this.symbols = symbols;
        }

        public Symbol getSymbol() {
            return Symbol.this;
        }
    }
}
