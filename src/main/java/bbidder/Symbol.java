package bbidder;

import java.util.Map;

public interface Symbol {
    public Symbol evaluate(SymbolTable symbols);

    public int getResolved();
    
    public boolean compatibleWith(Bid bid);
    
    public boolean nonConvential();
    
    Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols);
}
