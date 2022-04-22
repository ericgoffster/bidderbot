package bbidder.suitsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class Gt implements SuitSet {
    final Symbol symbol;

    public Gt(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public short evaluate(Players players) {
        Integer st = symbol.getResolved();
        return (short) (0xf & ~((1 << (st + 1)) - 1));
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
        Gt other = (Gt) obj;
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return ">" + symbol;
    }

    @Override
    public List<SuitSetContext> resolveSymbols(SymbolTable symbols) {
        List<SuitSetContext> l = new ArrayList<>();
        for(var e: symbol.resolveSymbol(symbols).entrySet()) {
            l.add(new SuitSetContext(new Gt(e.getKey()), e.getValue()));
        }
        return l;
    }
}