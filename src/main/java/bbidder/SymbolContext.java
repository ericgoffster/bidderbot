package bbidder;

/**
 * Represents a symbol in the context of a symbol table.
 * @author goffster
 */
public final class SymbolContext {
    public final Symbol symbol;
    public final SymbolTable symbols;

    public SymbolContext(Symbol symbol, SymbolTable symbols) {
        super();
        this.symbol = symbol;
        this.symbols = symbols;
    }
}