package bbidder.symbols;

import java.util.List;

import bbidder.Bid;
import bbidder.BitUtil;
import bbidder.Constants;
import bbidder.ListUtil;
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
    public List<SymbolContext> resolveSymbols(SymbolTable symbols) {
        if (symbols.containsKey("M")) {
            return List.of(new SymbolContext(new ConstSymbol(symbols.get("M")), symbols));
        }
        if (symbols.containsKey("OM")) {
            return List.of(new SymbolContext(new ConstSymbol(otherMajor(symbols.get("OM"))), symbols));
        }
        return ListUtil.map(BitUtil.iterate(Constants.MAJORS & ~symbols.values()), s -> new SymbolContext(new ConstSymbol(s), symbols.add("M", s)));
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
