package bbidder.symbols;

import java.util.Objects;

import bbidder.Constants;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.BitUtil;
import bbidder.utils.MyStream;

public final class VarSymbol extends Symbol {
    private final String varName;

    public VarSymbol(String varName) {
        super();
        this.varName = varName;
    }
    
    @Override
    public String toString() {
        return varName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VarSymbol other = (VarSymbol) obj;
        return Objects.equals(varName, other.varName);
    }

    @Override
    public int getResolvedStrain() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        Integer suit = suitTable.getSuit(varName);
        if (suit != null) {
            return MyStream.of(new ConstSymbol(suit).new Context(suitTable));
        }
        return BitUtil.stream((short) (Constants.ALL_SUITS & ~suitTable.getSuits()))
                .map(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded(varName, s)));
    }
}
