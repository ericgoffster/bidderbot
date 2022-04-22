package bbidder.generalities;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;

public final class AndGenerality extends Generality {
    private final Generality g1;
    private final Generality g2;

    public AndGenerality(Generality g1, Generality g2) {
        super();
        this.g1 = g1;
        this.g2 = g2;
    }

    public static Generality create(Generality g1, Generality g2) {
        if (g1.equals(TrueGenerality.T)) {
            return g2;
        }
        if (g2.equals(TrueGenerality.T)) {
            return g1;
        }
        return new AndGenerality(g1, g2);
    }

    @Override
    public Stream<Context> resolveSymbols(SuitTable suitTable) {
        return g1.resolveSymbols(suitTable)
                .flatMap(e1 -> g2.resolveSymbols(e1.suitTable)
                        .map(e2 -> new AndGenerality(e1.getGenerality(), e2.getGenerality()).new Context(e2.suitTable)));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        return g1.test(players, bidList) && g2.test(players, bidList);
    }

    @Override
    public String toString() {
        return g1 + ", " + g2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(g1, g2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AndGenerality other = (AndGenerality) obj;
        return Objects.equals(g1, other.g1) && Objects.equals(g2, other.g2);
    }
}
