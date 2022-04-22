package bbidder.symbols;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;
import bbidder.utils.BitUtil;

public final class OtherMajorSymbol extends Symbol {
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
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public Stream<Context> resolveSymbols(SymbolTable symbols) {
        if (symbols.containsKey("OM")) {
            return Stream.of(new ConstSymbol(symbols.get("OM")).new Context(symbols));
        }
        if (symbols.containsKey("M")) {
            return Stream.of(new ConstSymbol(otherMajor(symbols.get("M"))).new Context(symbols));
        }
        return StreamSupport.stream(BitUtil.iterate(Constants.MAJORS & ~symbols.values()).spliterator(), false)
                .map(s -> new ConstSymbol(s).new Context(symbols.add("OM", s)));
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
