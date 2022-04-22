package bbidder.suitsets;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.SymbolTable;

public final class Not implements SuitSet {
    private final SuitSet ss;

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
    public Stream<SuitSetContext> resolveSymbols(SymbolTable symbols) {
        return ss.resolveSymbols(symbols).map(e -> new SuitSetContext(new Not(e.suitSet), e.symbols));
    }
}