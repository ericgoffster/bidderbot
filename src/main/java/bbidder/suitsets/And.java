package bbidder.suitsets;

import java.util.Map;
import java.util.Objects;

import bbidder.Players;
import bbidder.SuitSet;

public class And implements SuitSet {
    final SuitSet s1;
    final SuitSet s2;

    public And(SuitSet s1, SuitSet s2) {
        super();
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public short evaluate(Players players) {
        return (short) (s1.evaluate(players) & s2.evaluate(players));
    }

    @Override
    public int hashCode() {
        return Objects.hash(s1, s2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        And other = (And) obj;
        return Objects.equals(s1, other.s1) && Objects.equals(s2, other.s2);
    }

    @Override
    public String toString() {
        return s1 + " & " + s2;
    }

    @Override
    public SuitSet replaceVars(Map<String, Integer> bc) {
        return new And(s1.replaceVars(bc), s2.replaceVars(bc));
    }
}