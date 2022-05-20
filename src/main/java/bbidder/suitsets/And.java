package bbidder.suitsets;

import java.util.Objects;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class And extends SuitSet {
    private final SuitSet s1;
    private final SuitSet s2;

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
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return s1.resolveSuits(suitTable)
                .flatMap(e1 -> s2.resolveSuits(e1.suitTable).map(e2 -> new And(e1.getSuitSet(), e2.getSuitSet()).new Context(e2.suitTable)));
    }

}