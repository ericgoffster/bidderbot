package bbidder.symbols;

import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Strain;
import bbidder.Symbol;
import bbidder.SymbolTable;

public class ConstSymbol implements Symbol {
    public final int strain;

    public ConstSymbol(int strain) {
        super();
        this.strain = strain;
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
    public Symbol evaluate(SymbolTable suits) {
        return this;
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        if (strain != this.strain) {
            throw new IllegalArgumentException();
        }
        return SymbolTable.EMPTY;
    }
    
    @Override
    public List<Symbol> boundSymbols(SymbolTable suits) {
        return List.of(this);
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
