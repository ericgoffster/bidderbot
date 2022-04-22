package bbidder;

/**
 * Represents a resolved bid pattern list in the context of a symbol table.
 * @author goffster
 */
public final class BidPatternListContext {
    public final BidPatternList bids;
    public final SymbolTable suits;

    public BidPatternListContext(BidPatternList bids, SymbolTable suits) {
        super();
        this.bids = bids;
        this.suits = suits;
    }
}