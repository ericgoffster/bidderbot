package bbidder.symbols;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class VarSymbol implements Symbol {
    public final String varName;

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
    public Symbol evaluate(SymbolTable symbols) {
        if (symbols.containsKey(varName)) {
            return new ConstSymbol(symbols.get(varName));
        }
        return null;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return SymbolTable.EMPTY.add(varName, strain);
    }

    @Override
    public List<Symbol> boundSymbols(SymbolTable symbols) {
        if (symbols.containsKey(varName)) {
            return List.of(evaluate(symbols));
        }
        return List.of(new ConstSymbol(Constants.CLUB), new ConstSymbol(Constants.DIAMOND), new ConstSymbol(Constants.HEART), new ConstSymbol(Constants.SPADE));
    }
    
    @Override
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        if (symbols.containsKey(varName)) {
            return Map.of(new ConstSymbol(symbols.get(varName)), symbols);
        }
        return Map.of(
                new ConstSymbol(Constants.CLUB), symbols.add(varName, Constants.CLUB),
                new ConstSymbol(Constants.DIAMOND), symbols.add(varName, Constants.DIAMOND),
                new ConstSymbol(Constants.HEART), symbols.add(varName, Constants.HEART),
                new ConstSymbol(Constants.SPADE), symbols.add(varName, Constants.SPADE));
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
