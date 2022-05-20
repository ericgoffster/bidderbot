package bbidder.generalities;

import java.util.HashSet;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;

import bbidder.Generality;
import bbidder.Players;
import bbidder.Position;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.TaggedAuction;
import bbidder.utils.MyStream;

public final class BestFitEstablished extends Generality {
    public static final String NAME = "bestfit";
    private final Symbol symbol;
    private final SuitLengthRange combined;

    public BestFitEstablished(Symbol symbol, SuitLengthRange combined) {
        super();
        this.symbol = symbol;
        this.combined = combined;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new BestFitEstablished(e.getSymbol(), combined).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, TaggedAuction bidList) {
        return getBestSuits(players).contains(symbol.getResolvedStrain());
    }

    private Set<Integer> getBestSuits(Players players) {
        Position position = Position.ME;
        Set<Integer> bestSuits = new HashSet<>();
        int bestLen = 0;
        for (int t = 0; t < 4; t++) {
            OptionalInt minLenInSuitPartner = players.getPlayer(position.getOpposite()).infSummary.minLenInSuit(t);
            if (!minLenInSuitPartner.isPresent()) {
                return Set.of();
            }
            OptionalInt minLenInSuitMe = players.getPlayer(position).infSummary.minLenInSuit(t);
            if (!minLenInSuitPartner.isPresent()) {
                return Set.of();
            }
            int combinedLen = minLenInSuitMe.getAsInt() + minLenInSuitPartner.getAsInt();
            if (combined.contains(combinedLen)) {
                if (combinedLen > bestLen) {
                    bestSuits.clear();
                    bestLen = combinedLen;
                    bestSuits.add(t);
                } else if (combinedLen == bestLen) {
                    bestSuits.add(t);
                }
            }
        }
        return bestSuits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(combined, symbol);
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
        return Objects.equals(combined, other.combined) && Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return combined + " " + NAME + " " + symbol;
    }

}
