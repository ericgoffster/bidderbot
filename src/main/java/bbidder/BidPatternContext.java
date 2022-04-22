package bbidder;

/**
 * Represents a resolved bid pattern in the context of a symbol table.
 * @author goffster
 */
public final class BidPatternContext {
    public final BidPattern bidPattern;
    public final SymbolTable suits;

    public BidPatternContext(BidPattern bidPattern, SymbolTable suits) {
        super();
        this.bidPattern = bidPattern;
        this.suits = suits;
    }
}