package bbidder.generalities;

import java.util.List;
import java.util.Objects;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.GeneralityContext;
import bbidder.ListUtil;
import bbidder.Players;
import bbidder.SymbolTable;

public final class AndGenerality implements Generality {
    public final Generality g1;
    public final Generality g2;

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
    public List<GeneralityContext> resolveSymbols(SymbolTable symbols) {
        return ListUtil.flatMap(g1.resolveSymbols(symbols), e1 -> ListUtil.map(g2.resolveSymbols(e1.symbols),
                e2 -> new GeneralityContext(new AndGenerality(e1.generality, e2.generality), e2.symbols)));
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
