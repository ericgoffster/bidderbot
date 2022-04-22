package bbidder;

/**
 * Represents a bound Generality in the context of a symbol table.
 * @author goffster
 *
 */
public final class GeneralityContext {
    public final Generality generality;
    public final SymbolTable suits;

    public GeneralityContext(Generality generality, SymbolTable suits) {
        super();
        this.generality = generality;
        this.suits = suits;
    }
}