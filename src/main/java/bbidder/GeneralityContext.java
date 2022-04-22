package bbidder;

/**
 * Represents a bound Generality in the context of a symbol table.
 * @author goffster
 *
 */
public final class GeneralityContext {
    public final Generality generality;
    public final SymbolTable symbols;

    public GeneralityContext(Generality generality, SymbolTable symbols) {
        super();
        this.generality = generality;
        this.symbols = symbols;
    }
}