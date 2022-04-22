package bbidder.symbols;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SuitTable;
import bbidder.utils.BitUtil;

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
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public Stream<Context> resolveSymbols(SuitTable suitTable) {
        Integer suit = suitTable.getSuit(varName);
        if (suit != null) {
            return Stream.of(new ConstSymbol(suit).new Context(suitTable));
        }
        return StreamSupport.stream(BitUtil.iterate(Constants.ALL_SUITS & ~suitTable.getSuits()).spliterator(), false)
                .map(s -> new ConstSymbol(s).new Context(suitTable.withSuitAdded(varName, s)));
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
