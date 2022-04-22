package bbidder;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolContext {
    public static Map<Symbol, SymbolTable> resolveSymbols(SymbolTable suits, Symbol symbol) {
        {
            Symbol esymbol = symbol.evaluate(suits);
            if (esymbol != null) {
                int strain = esymbol.getResolved();
                if (strain < 0 || strain > 4) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return Map.of(esymbol, suits);
            }
        }
        Map<Symbol, SymbolTable> m = new LinkedHashMap<Symbol, SymbolTable>();
        for (Symbol newSym: symbol.boundSymbols(suits)) {
            if (!suits.containsValue(newSym.getResolved())) {
                m.put(newSym, suits.add(symbol.unevaluate(newSym.getResolved())));
            }
        }
        return m;
    }
}
