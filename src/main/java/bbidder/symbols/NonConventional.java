package bbidder.symbols;

import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;
import bbidder.utils.ListUtil;

public final class NonConventional extends Symbol {
    private final Symbol symbol;

    public NonConventional(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol + ":nonconventional";
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
        NonConventional other = (NonConventional) obj;
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public List<Context> resolveSymbols(SymbolTable symbols) {
        return ListUtil.map(symbol.resolveSymbols(symbols), e -> new NonConventional(e.getSymbol()).new Context(e.symbols));
    }

    @Override
    public int getResolved() {
        return symbol.getResolved();
    }

    @Override
    public boolean compatibleWith(Bid bid) {
        return symbol.compatibleWith(bid);
    }

    @Override
    public boolean nonConvential() {
        return true;
    }
}
