package bbidder;

public final class BidPatternContext {
    public final BidPattern bid;

    public final SymbolTable suits;

    public BidPatternContext(BidPattern bid, SymbolTable suits) {
        super();
        this.bid = bid;
        this.suits = suits;
    }
}