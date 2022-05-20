package bbidder.symbols;

import bbidder.Constants;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.BitUtil;
import bbidder.utils.MyStream;

public final class MajorSymbol extends Symbol {
    public static final String NAME = "M";

    public MajorSymbol() {
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
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        Integer M = suitTable.getSuit(NAME);
        if (M != null) {
            return MyStream.of(new ConstSymbol(M).new Context(suitTable));
        }
        Integer OM = suitTable.getSuit(OtherMajorSymbol.NAME);
        if (OM != null) {
            return MyStream.of(new ConstSymbol(otherMajor(OM)).new Context(suitTable));
        }
        return BitUtil.stream((short) (Constants.MAJORS & ~suitTable.getSuits()))
                .map(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded(NAME, s)));
    }

    static Integer otherMajor(Integer strain) {
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
}
