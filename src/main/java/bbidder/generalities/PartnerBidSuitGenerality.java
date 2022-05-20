package bbidder.generalities;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class PartnerBidSuitGenerality extends Generality {
    public static final String NAME = "partner_bid_suit";
    private final Symbol symbol;

    public PartnerBidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new PartnerBidSuitGenerality(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int suit = symbol.getResolvedStrain();
        return players.flip().iBidSuit(suit);
    }

    @Override
    public String toString() {
        return NAME + " " + symbol;
    }

}
