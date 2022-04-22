package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;

public class GreaterThanSymbol implements Symbol {
    public final Symbol sym;
    public final int level;
    public final Symbol other;

    public GreaterThanSymbol(Symbol sym, int level, Symbol other) {
        super();
        this.sym = sym;
        this.level = level;
        this.other = other;
    }

    @Override
    public String toString() {
        return sym + ":>"+level+other;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, sym, other);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GreaterThanSymbol other = (GreaterThanSymbol) obj;
        return level == other.level && Objects.equals(sym, other.sym) && Objects.equals(other, other.other);
    }

    @Override
    public Symbol evaluate(SymbolTable suits) {
        Symbol evaluate = sym.evaluate(suits);
        if (evaluate == null) {
            return null;
        }
        return new GreaterThanSymbol(evaluate, level, other);
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return sym.unevaluate(strain);
    }
    
    @Override
    public List<Symbol> boundSymbols(SymbolTable suits) {
        List<Symbol> boundOthers = other.boundSymbols(suits);
        if (boundOthers.size() != 1) {
            throw new IllegalArgumentException("undefined smybol: " + other);
        }
        List<Symbol> old = sym.boundSymbols(suits);
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: old) {
            l.add(new GreaterThanSymbol(s, level, boundOthers.get(0)));
        }
        return l;
    }

    @Override
    public int getResolved() {
        return sym.getResolved();
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        Bid comparisonBid = Bid.valueOf(level, other.getResolved());
        return sym.compatibleWith(bid) && bid.compareTo(comparisonBid) > 0;
    }
    
    @Override
    public boolean nonConvential() {
        return sym.nonConvential();
    }
}
