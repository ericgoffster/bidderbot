package bbidder.symbols;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bbidder.Bid;
import bbidder.BitUtil;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class OtherMinorSymbol implements Symbol {
    public OtherMinorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return "om";
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
        if (symbols.containsKey("m")) {
            return new ConstSymbol(otherMinor(symbols.get("m")));
        }
        if (symbols.containsKey("om")) {
            return new ConstSymbol(symbols.get("om"));
        }
        return null;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public List<Symbol> boundSymbols(SymbolTable symbols) {
        if (symbols.containsKey("m") || symbols.containsKey("om")) {
            return List.of(evaluate(symbols));
        }
        return List.of(new ConstSymbol(Constants.CLUB), new ConstSymbol(Constants.DIAMOND));
    }
    
    @Override
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        if (symbols.containsKey("om")) {
            return Map.of(new ConstSymbol(symbols.get("om")), symbols);
        }
        if (symbols.containsKey("m")) {
            return Map.of(new ConstSymbol(otherMinor(symbols.get("m"))), symbols);
        }
        LinkedHashMap<Symbol, SymbolTable> map = new LinkedHashMap<>();
        for(int s: BitUtil.iterate(Constants.MINORS)) {
            if (!symbols.containsValue(s)) {
                map.put(new ConstSymbol(s), symbols.add("om", s));
            }
        }
        return map;
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
