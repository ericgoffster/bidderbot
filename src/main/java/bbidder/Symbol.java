package bbidder;

import java.util.Map;

public interface Symbol {
    /**
     * @return The resolved value
     */
    int getResolved();
    
    /**
     * @param bid The bid to test
     * @return true if the given bid is compatible with the symbol
     */
    boolean compatibleWith(Bid bid);
    
    /**
     * 
     * @return true if the given bid should only be considered if the bid was not previously defined.
     */
    boolean nonConvential();
    
    Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols);
}
