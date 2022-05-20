package bbidder.symbols;

import java.util.stream.Stream;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.BitUtil;

public final class OtherMajorSymbol extends Symbol {
    public static final String NAME = "OM";

    public OtherMajorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return NAME;
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
        Integer OM = suitTable.getSuit(NAME);
        if (OM != null) {
            return Stream.of(new ConstSymbol(OM).new Context(suitTable));
        }
        Integer M = suitTable.getSuit(MajorSymbol.NAME);
        if (M != null) {
            return Stream.of(new ConstSymbol(MajorSymbol.otherMajor(M)).new Context(suitTable));
        }
        return BitUtil.stream((short)(Constants.MAJORS & ~suitTable.getSuits()))
                .mapToObj(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded(NAME, s)));
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
