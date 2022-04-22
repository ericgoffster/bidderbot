package bbidder;

import java.util.Map;

public final class SymbolContext {
    public static Map<Symbol, SymbolTable> resolveSymbols(SymbolTable symbols, Symbol symbol) {
        return symbol.resolveSymbol(symbols);
    }
}
