package bbidder.symbols;

import bbidder.Constants;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.BitUtil;
import bbidder.utils.MyStream;

public final class MinorSymbol extends Symbol {
    public static final String NAME = "m";

    public MinorSymbol() {
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
    public MyStream<ConstSymbol.Context> resolveSuits(SuitTable suitTable) {
        Integer m = suitTable.getSuit(NAME);
        if (m != null) {
            return MyStream.of(new ConstSymbol(m).new Context(suitTable));
        }
        Integer om = suitTable.getSuit(OtherMinorSymbol.NAME);
        if (om != null) {
            return MyStream.of(new ConstSymbol(otherMinor(om)).new Context(suitTable));
        }
        return BitUtil.stream((short) (Constants.MINORS & ~suitTable.getSuits()))
                .map(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded(NAME, s)));
    }

    public static Integer otherMinor(Integer strain) {
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
}
