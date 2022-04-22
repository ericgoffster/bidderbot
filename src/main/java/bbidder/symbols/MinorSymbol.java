package bbidder.symbols;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;
import bbidder.utils.BitUtil;

public final class MinorSymbol extends Symbol {
    public MinorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return "m";
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
        if (symbols.containsKey("m")) {
            return Stream.of(new ConstSymbol(symbols.get("m")).new Context(symbols));
        }
        if (symbols.containsKey("om")) {
            return Stream.of(new ConstSymbol(otherMinor(symbols.get("om"))).new Context(symbols));
        }
        return StreamSupport.stream(BitUtil.iterate(Constants.MINORS & ~symbols.values()).spliterator(), false)
                .map(s -> new ConstSymbol(s).new Context(symbols.add("m", s)));
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
