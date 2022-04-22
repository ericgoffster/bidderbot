package bbidder.symbols;

import java.util.LinkedHashMap;
import java.util.Map;

import bbidder.Bid;
import bbidder.BitUtil;
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
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        if (symbols.containsKey("M")) {
            return Map.of(new ConstSymbol(symbols.get("M")), symbols);
        }
        if (symbols.containsKey("OM")) {
            return Map.of(new ConstSymbol(otherMajor(symbols.get("OM"))), symbols);
        }
        LinkedHashMap<Symbol, SymbolTable> map = new LinkedHashMap<>();
        for(int s: BitUtil.iterate(Constants.MAJORS)) {
            if (!symbols.containsValue(s)) {
                map.put(new ConstSymbol(s), symbols.add("M", s));
            }
        }
        return map;
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
