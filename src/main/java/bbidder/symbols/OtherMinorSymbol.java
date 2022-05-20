package bbidder.symbols;

import java.util.stream.Stream;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.BitUtil;

public final class OtherMinorSymbol extends Symbol {
    public static final String NAME = "om";

    public OtherMinorSymbol() {
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
        Integer om = suitTable.getSuit(NAME);
        if (om != null) {
            return Stream.of(new ConstSymbol(om).new Context(suitTable));
        }
        Integer m = suitTable.getSuit(MinorSymbol.NAME);
        if (m != null) {
            return Stream.of(new ConstSymbol(MinorSymbol.otherMinor(m)).new Context(suitTable));
        }
        return BitUtil.stream((short)(Constants.MINORS & ~suitTable.getSuits()))
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
