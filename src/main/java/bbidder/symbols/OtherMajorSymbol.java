package bbidder.symbols;

import bbidder.Constants;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.BitUtil;
import bbidder.utils.MyStream;

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
    public MyStream<ConstSymbol.Context> resolveSuits(SuitTable suitTable) {
        Integer OM = suitTable.getSuit(NAME);
        if (OM != null) {
            return MyStream.of(new ConstSymbol(OM).new Context(suitTable));
        }
        Integer M = suitTable.getSuit(MajorSymbol.NAME);
        if (M != null) {
            OM = MajorSymbol.otherMajor(M);
            if ((suitTable.getSuits() & (1 << OM)) != 0) {
                return MyStream.empty();
            }
            return MyStream.of(new ConstSymbol(OM).new Context(suitTable.withSuitAdded(NAME, OM)));
        }
        return BitUtil.stream((short) (Constants.MAJORS & ~suitTable.getSuits()))
                .map(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded(NAME, s)));
    }
}
