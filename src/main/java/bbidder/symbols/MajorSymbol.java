package bbidder.symbols;

import java.util.List;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class MajorSymbol implements Symbol {
    public MajorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return "M";
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
        if (suits.containsKey("OM")) {
            return new ConstSymbol(otherMajor(suits.get("OM")));
        }
        if (suits.containsKey("M")) {
            return new ConstSymbol(suits.get("M"));
        }
        return null;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return SymbolTable.EMPTY.add("M", strain);
    }
    
    @Override
    public List<Symbol> boundSymbols(SymbolTable suits) {
        if (suits.containsKey("M") || suits.containsKey("OM")) {
            return List.of(evaluate(suits));
        }
        return List.of(new ConstSymbol(Constants.HEART), new ConstSymbol(Constants.SPADE));
    }

    private static Integer otherMajor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.HEART:
            return Constants.SPADE;
        case Constants.SPADE:
            return Constants.HEART;
        default:
            throw new IllegalArgumentException("invalid major");
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
