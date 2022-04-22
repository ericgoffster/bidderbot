package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.BitUtil;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolContext;
import bbidder.SymbolTable;

public final class VarSymbol implements Symbol {
    public final String varName;

    public VarSymbol(String varName) {
        super();
        this.varName = varName;
    }

    @Override
    public String toString() {
        return varName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName);
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
        return Objects.equals(varName, other.varName);
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public List<SymbolContext> resolveSymbols(SymbolTable symbols) {
        if (symbols.containsKey(varName)) {
            return List.of(new SymbolContext(new ConstSymbol(symbols.get(varName)), symbols));
        }
        List<SymbolContext> l = new ArrayList<>();
        for(int s: BitUtil.iterate(Constants.ALL_SUITS ^ symbols.values())) {
            l.add(new SymbolContext(new ConstSymbol(s), symbols.add(varName, s)));
        }
        return l;
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
