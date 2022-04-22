package bbidder;

/**
 * Represents a suitsets in the context of a symbol table.
 * 
 * @author goffster
 *
 */
public final class SuitSetContext {
    public final SuitSet suitSet;
    public final SymbolTable symbols;

    public SuitSetContext(SuitSet ss, SymbolTable symbols) {
        super();
        this.suitSet = ss;
        this.symbols = symbols;
    }
}