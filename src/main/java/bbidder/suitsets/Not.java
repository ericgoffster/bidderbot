package bbidder.suitsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.SymbolTable;

public final class Not implements SuitSet {
    final SuitSet ss;

    public Not(SuitSet ss) {
        super();
        this.ss = ss;
    }

    @Override
    public short evaluate(Players players) {
        return (short) (0xf & ~ss.evaluate(players));
    }

    @Override
    public int hashCode() {
        return Objects.hash(ss);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Not other = (Not) obj;
        return Objects.equals(ss, other.ss);
    }

    @Override
    public String toString() {
        return "~" + ss;
    }

    @Override
    public List<SuitSetContext> resolveSymbols(SymbolTable symbols) {
        List<SuitSetContext> l = new ArrayList<>();
        for(var e: ss.resolveSymbols(symbols)) {
            l.add(new SuitSetContext(new Not(e.ss), e.symbols));
        }
        return l;
    }
}