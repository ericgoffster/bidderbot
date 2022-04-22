package bbidder.symbols;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SuitTable;
import bbidder.utils.BitUtil;

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
    public int getResolvedStrain() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        Integer om = suitTable.getSuit("om");
        if (om != null) {
            return Stream.of(new ConstSymbol(om).new Context(suitTable));
        }
        Integer m = suitTable.getSuit("m");
        if (m != null) {
            return Stream.of(new ConstSymbol(otherMinor(m)).new Context(suitTable));
        }
        return StreamSupport.stream(BitUtil.iterate(Constants.MINORS & ~suitTable.getSuits()).spliterator(), false)
                .map(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded("om", s)));
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
    public boolean isNonConvential() {
        return false;
    }
}
