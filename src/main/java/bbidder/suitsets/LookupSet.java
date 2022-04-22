package bbidder.suitsets;

import java.util.Objects;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSets;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class LookupSet implements SuitSet {
    final Symbol strain;

    public LookupSet(Symbol strain) {
        this.strain = strain;
    }

    @Override
    public short evaluate(Players players) {
        int st = strain.getResolved();
        return (short) (1 << st);
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
        LookupSet other = (LookupSet) obj;
        return Objects.equals(strain, other.strain);
    }

    @Override
    public String toString() {
        return strain.toString();
    }

    @Override
    public SuitSet replaceVars(SymbolTable bc) {
        return new LookupSet(SuitSets.bind(bc, strain));
    }
}