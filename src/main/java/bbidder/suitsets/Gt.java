package bbidder.suitsets;

import java.util.Map;
import java.util.Objects;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSets;
import bbidder.Symbol;

public class Gt implements SuitSet {
    final Symbol strain;

    public Gt(Symbol strain) {
        super();
        this.strain = strain;
    }

    @Override
    public short evaluate(Players players) {
        Integer st = strain.getResolved();
        return (short) (0xf & ~((1 << (st + 1)) - 1));
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
        Gt other = (Gt) obj;
        return Objects.equals(strain, other.strain);
    }

    @Override
    public String toString() {
        return ">" + strain;
    }

    @Override
    public SuitSet replaceVars(Map<String, Integer> bc) {
        return new Gt(SuitSets.bind(bc, strain));
    }
}