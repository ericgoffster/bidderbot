package bbidder;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SymbolContext {
    public static Map<Symbol, SymbolTable> resolveSymbols(SymbolTable symbols, Symbol symbol) {
        {
            Symbol esymbol = symbol.evaluate(symbols);
            if (esymbol != null) {
                int strain = esymbol.getResolved();
                if (strain < 0 || strain > 4) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return Map.of(esymbol, symbols);
            }
        }
        Map<Symbol, SymbolTable> m = new LinkedHashMap<Symbol, SymbolTable>();
        for (Symbol newSym: symbol.boundSymbols(symbols)) {
            if (!symbols.containsValue(newSym.getResolved())) {
                m.put(newSym, symbols.add(symbol.unevaluate(newSym.getResolved())));
            }
        }
        return m;
    }
}
