package bbidder;

import java.util.List;

public interface Symbol {
    public Symbol evaluate(SymbolTable suits);

    public SymbolTable unevaluate(int strain);

    public List<Symbol> boundSymbols(SymbolTable suits);

    public int getResolved();
    
    public boolean compatibleWith(Bid bid);
    
    public boolean nonConvential();
}
