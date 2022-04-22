package bbidder.symbols;

import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;

public class VarSymbol implements Symbol {
    public final String v;

    public VarSymbol(String v) {
        super();
        this.v = v;
    }

    @Override
    public String toString() {
        return v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VarSymbol other = (VarSymbol) obj;
        return Objects.equals(v, other.v);
    }

    @Override
    public Symbol evaluate(SymbolTable suits) {
        if (suits.containsKey(v)) {
            return new ConstSymbol(suits.get(v));
        }
        return null;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return SymbolTable.EMPTY.add(v, strain);
    }

    @Override
    public List<Symbol> boundSymbols(SymbolTable suits) {
        if (suits.containsKey(v)) {
            return List.of(evaluate(suits));
        }
        return List.of(new ConstSymbol(Constants.CLUB), new ConstSymbol(Constants.DIAMOND), new ConstSymbol(Constants.HEART), new ConstSymbol(Constants.SPADE));
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
