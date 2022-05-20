package bbidder.generalities;

import java.util.Optional;
import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SuitTable;
import bbidder.utils.SplitUtil;

public final class PartnerBidSuitGenerality extends Generality {
    private final Symbol symbol;

    public PartnerBidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new PartnerBidSuitGenerality(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int suit = symbol.getResolvedStrain();
        Optional<Integer> meLen = players.me.infSummary.minLenInSuit(suit);
        Optional<Integer> partnerLen = players.partner.infSummary.minLenInSuit(suit);
        if (!meLen.isPresent() || !partnerLen.isPresent()) {
            return false;
        }
        if (meLen.get() + partnerLen.get() >= 8) {
            return false;
        }
        if ((players.partner.infSummary.getBidSuits() & (1 << suit)) == 0) {
            return false;
        }
        return true;
    }

    public static PartnerBidSuitGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("partner_bid_suit")) {
            Symbol symbol = SymbolParser.parseSymbol(parts[1]);
            if (symbol != null) {
                return new PartnerBidSuitGenerality(symbol);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "partner_bid_suit " + symbol;
    }

}
