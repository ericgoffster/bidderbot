package bbidder.symbols;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bbidder.Bid;
import bbidder.BitUtil;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class OtherMajorSymbol implements Symbol {
    public OtherMajorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return "OM";
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
    public Symbol evaluate(SymbolTable symbols) {
        if (symbols.containsKey("M")) {
            return new ConstSymbol(otherMajor(symbols.get("M")));
        }
        if (symbols.containsKey("OM")) {
            return new ConstSymbol(symbols.get("OM"));          
        }
        return null;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public List<Symbol> boundSymbols(SymbolTable symbols) {
        if (symbols.containsKey("M") || symbols.containsKey("OM")) {
            return List.of(evaluate(symbols));
        }
        return List.of(new ConstSymbol(Constants.HEART), new ConstSymbol(Constants.SPADE));
    }
    
    @Override
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        if (symbols.containsKey("OM")) {
            return Map.of(new ConstSymbol(symbols.get("OM")), symbols);
        }
        if (symbols.containsKey("M")) {
            return Map.of(new ConstSymbol(otherMajor(symbols.get("M"))), symbols);
        }
        LinkedHashMap<Symbol, SymbolTable> map = new LinkedHashMap<>();
        for(int s: BitUtil.iterate(Constants.MAJORS)) {
            if (!symbols.containsValue(s)) {
                map.put(new ConstSymbol(s), symbols.add("OM", s));
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
