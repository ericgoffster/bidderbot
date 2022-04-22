package bbidder.symbols;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Bid;
import bbidder.Strain;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class ConstSymbol extends Symbol {
    private final int strain;

    public ConstSymbol(int strain) {
        super();
        this.strain = strain;
    }

    @Override
    public Stream<Context> resolveSymbols(SymbolTable symbols) {
        return Stream.of(new Context(symbols));
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
    public int getResolved() {
        return strain;
    }

    @Override
    public boolean compatibleWith(Bid bid) {
        return bid.strain == strain;
    }

    @Override
    public boolean nonConvential() {
        return false;
    }
}
