package bbidder.symbols;

import java.util.List;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;
import bbidder.utils.BitUtil;
import bbidder.utils.ListUtil;

public final class OtherMinorSymbol extends Symbol {
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
    public List<Context> resolveSymbols(SymbolTable symbols) {
        if (symbols.containsKey("om")) {
            return List.of(new ConstSymbol(symbols.get("om")).new Context(symbols));
        }
        if (symbols.containsKey("m")) {
            return List.of(new ConstSymbol(otherMinor(symbols.get("m"))).new Context(symbols));
        }
        return ListUtil.map(BitUtil.iterate(Constants.MINORS & ~symbols.values()), s -> new ConstSymbol(s).new Context(symbols.add("om", s)));
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
