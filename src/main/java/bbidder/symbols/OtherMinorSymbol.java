package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;

import bbidder.Bid;
import bbidder.BitUtil;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolContext;
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
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public List<SymbolContext> resolveSymbols(SymbolTable symbols) {
        if (symbols.containsKey("om")) {
            return List.of(new SymbolContext(new ConstSymbol(symbols.get("om")), symbols));
        }
        if (symbols.containsKey("m")) {
            return List.of(new SymbolContext(new ConstSymbol(otherMinor(symbols.get("m"))), symbols));
        }
        List<SymbolContext> l = new ArrayList<>();
        for(int s: BitUtil.iterate(Constants.MINORS & ~symbols.values())) {
            l.add(new SymbolContext(new ConstSymbol(s), symbols.add("om", s)));
        }
        return l;
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
