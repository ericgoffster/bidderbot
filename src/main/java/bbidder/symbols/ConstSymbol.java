package bbidder.symbols;

import java.util.Objects;

import bbidder.Strain;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class ConstSymbol extends Symbol {
    private final int strain;

    public ConstSymbol(int strain) {
        super();
        this.strain = strain;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return Strain.getName(strain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strain);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstSymbol other = (ConstSymbol) obj;
        return strain == other.strain;
    }

    @Override
    public int getResolvedStrain() {
        return strain;
    }
    
    public final class Context {
        public final SuitTable suitTable;

        public Context(SuitTable suitTable) {
            super();
            this.suitTable = suitTable;
        }

        public ConstSymbol getSymbol() {
            return ConstSymbol.this;
        }
    }
}
