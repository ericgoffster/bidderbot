package bbidder.symbols;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SuitTable;
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
    public Stream<Context> resolveSymbols(SuitTable suitTable) {
        if (suitTable.containsName("OM")) {
            return Stream.of(new ConstSymbol(suitTable.getSuit("OM")).new Context(suitTable));
        }
        if (suitTable.containsName("M")) {
            return Stream.of(new ConstSymbol(otherMajor(suitTable.getSuit("M"))).new Context(suitTable));
        }
        return StreamSupport.stream(BitUtil.iterate(Constants.MAJORS & ~suitTable.getSuits()).spliterator(), false)
                .map(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded("OM", s)));
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
