package bbidder.symbols;

import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;
import bbidder.utils.BitUtil;
import bbidder.utils.ListUtil;

public final class VarSymbol extends Symbol {
    private final String varName;

    public VarSymbol(String varName) {
        super();
        this.varName = varName;
    }

    @Override
    public String toString() {
        return varName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VarSymbol other = (VarSymbol) obj;
        return Objects.equals(varName, other.varName);
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public List<Context> resolveSymbols(SymbolTable symbols) {
        if (symbols.containsKey(varName)) {
            return List.of(new ConstSymbol(symbols.get(varName)).new Context(symbols));
        }
        return ListUtil.map(BitUtil.iterate(Constants.ALL_SUITS & ~symbols.values()), s -> new ConstSymbol(s).new Context(symbols.add(varName, s)));
    }

    @Override
    public boolean compatibleWith(Bid bid) {
        return true;
    }

    @Override
    public boolean nonConvential() {
        return false;
    }
}
