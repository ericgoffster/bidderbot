package bbidder.generalities;

import java.util.HashSet;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class BestFitEstablished extends Generality {
    public static final String NAME = "bestfit_established";
    private final Symbol symbol;

    public BestFitEstablished(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new BestFitEstablished(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        Set<Integer> bestSuits = new HashSet<>();
        int bestLen = 0;
        for(int t = 0; t < 4; t++) {
            OptionalInt minLenInSuitPartner = players.partner.infSummary.minLenInSuit(t);
            if (!minLenInSuitPartner.isPresent()) {
                return false;
            }
            OptionalInt minLenInSuitMe = players.me.infSummary.minLenInSuit(t);
            if (!minLenInSuitPartner.isPresent()) {
                return false;
            }
            int combinedLen = minLenInSuitMe.getAsInt() + minLenInSuitPartner.getAsInt();
            if (combinedLen >= 7) {
                if (combinedLen > bestLen) {
                    bestSuits.clear();
                    bestLen = combinedLen;
                    bestSuits.add(t);
                } else if (combinedLen == bestLen) {
                    bestSuits.add(t);
                }
            }
        }
        
        return bestSuits.contains(symbol.getResolvedStrain());
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BestFitEstablished other = (BestFitEstablished) obj;
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return "bestfit_established" + " " + symbol;
    }

}
