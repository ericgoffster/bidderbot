package bbidder.inferences;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitSet;
import bbidder.Symbol;
import bbidder.SuitTable;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a suit than is longest or equal among other suits.
 * 
 * @author goffster
 *
 */
public final class LongestOrEqual extends Inference {
    private final Symbol suit;
    private final SuitSet among;

    public LongestOrEqual(Symbol suit, SuitSet among) {
        this.suit = suit;
        this.among = among;
    }

    @Override
    public IBoundInference bind(Players players) {
        short iamong = among == null ? 0xf : among.evaluate(players);
        int strain = suit.getResolvedStrain();
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isLongerOrEqual(strain, iamong)));
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return suit.resolveSuits(suitTable)
                .flatMap(e1 -> among.resolveSuits(e1.suitTable).map(e2 -> new LongestOrEqual(e1.getSymbol(), e2.getSuitSet()).new Context(e2.suitTable)));
    }

    @Override
    public String toString() {
        return "longest_or_equal " + suit + (among == null ? "" : " among " + among);
    }

    @Override
    public int hashCode() {
        return Objects.hash(among, suit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LongestOrEqual other = (LongestOrEqual) obj;
        return Objects.equals(among, other.among) && Objects.equals(suit, other.suit);
    }
}
