package bbidder.symbols;

import java.util.List;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;

public class MinorSymbol implements Symbol {
    public MinorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return "m";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    @Override
    public Symbol evaluate(SymbolTable suits) {
        if (suits.containsKey("om")) {
            return new ConstSymbol(otherMinor(suits.get("om")));
        }
        if (suits.containsKey("m")) {
            return new ConstSymbol(suits.get("m"));
        }
        return null;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return SymbolTable.EMPTY.add("m", strain);
    }
    
    @Override
    public List<Symbol> boundSymbols(SymbolTable suits) {
        if (suits.containsKey("m") || suits.containsKey("om")) {
            return List.of(evaluate(suits));
        }
        return List.of(new ConstSymbol(Constants.CLUB), new ConstSymbol(Constants.DIAMOND));
    }

    private static Integer otherMinor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.CLUB:
            return Constants.DIAMOND;
        case Constants.DIAMOND:
            return Constants.CLUB;
        default:
            throw new IllegalArgumentException("invalid minor");
        }
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return true;
    }
    
    @Override
    public boolean nonConvential() {
        return false;
    }
}
