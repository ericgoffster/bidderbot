package bbidder;

import java.util.List;
import java.util.Map;

public interface Symbol {
    public Symbol evaluate(SymbolTable symbols);

    public SymbolTable unevaluate(int strain);

    public List<Symbol> boundSymbols(SymbolTable symbols);

    public int getResolved();
    
    public boolean compatibleWith(Bid bid);
    
    public boolean nonConvential();
    
    Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols);
}
