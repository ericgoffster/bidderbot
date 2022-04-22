package bbidder.suitsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class LookupSet implements SuitSet {
    final Symbol symbol;

    public LookupSet(Symbol strain) {
        this.symbol = strain;
    }

    @Override
    public short evaluate(Players players) {
        int st = symbol.getResolved();
        return (short) (1 << st);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
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
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return symbol.toString();
    }

    @Override
    public List<SuitSetContext> resolveSymbols(SymbolTable symbols) {
        List<SuitSetContext> l = new ArrayList<>();
        for(var e: symbol.resolveSymbol(symbols).entrySet()) {
            l.add(new SuitSetContext(new LookupSet(e.getKey()), e.getValue()));
        }
        return l;
    }
}