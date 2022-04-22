package bbidder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolContext {
    public static Map<Symbol, Map<String, Integer>> resolveSymbols(Map<String, Integer> suits, Symbol symbol) {
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
        Map<Symbol, Map<String, Integer>> m = new LinkedHashMap<Symbol, Map<String, Integer>>();
        for (Symbol newSym: symbol.boundSymbols(suits)) {
            if (!suits.containsValue(newSym.getResolved())) {
                Map<String, Integer> newSuits = new HashMap<>(suits);
                newSuits.putAll(symbol.unevaluate(newSym.getResolved()));
                m.put(newSym, newSuits);
            }
        }
        return m;
    }
}
