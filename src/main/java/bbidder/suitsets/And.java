package bbidder.suitsets;

import java.util.List;
import java.util.Objects;

import bbidder.ListUtil;
import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.SymbolTable;

public final class And implements SuitSet {
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
    public List<SuitSetContext> resolveSymbols(SymbolTable symbols) {
        return ListUtil.flatMap(s1.resolveSymbols(symbols), e1 -> ListUtil.map(s2.resolveSymbols(e1.symbols),
                e2 -> new SuitSetContext(new And(e1.suitSet, e2.suitSet), e2.symbols)));
    }

}