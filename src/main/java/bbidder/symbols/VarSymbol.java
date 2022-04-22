package bbidder.symbols;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.BitUtil;
import bbidder.Constants;
import bbidder.Symbol;
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
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        if (symbols.containsKey(varName)) {
            return Map.of(new ConstSymbol(symbols.get(varName)), symbols);
        }
        LinkedHashMap<Symbol, SymbolTable> map = new LinkedHashMap<>();
        for(int s: BitUtil.iterate(Constants.ALL_SUITS)) {
            if (!symbols.containsValue(s)) {
                map.put(new ConstSymbol(s), symbols.add(varName, s));
            }
        }
        return map;
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
