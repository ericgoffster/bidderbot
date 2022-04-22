package bbidder;

import java.util.Map;

public interface Symbol {
    public int getResolved();
    
    public boolean compatibleWith(Bid bid);
    
    public boolean nonConvential();
    
    Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols);
}
