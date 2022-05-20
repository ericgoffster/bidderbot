package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.MyStream;

/**
 * Represents the inference of a suit than is longest or equal among other suits.
 * 
 * @author goffster
 *
 */
public final class LongestOrEqual extends Inference {
    public static final String NAME = "longest_or_equal";
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
        if ((iamong & (1 << strain)) == 0) {
            return ConstBoundInference.F;
        }
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isLongerOrEqual(strain, iamong)));
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return suit.resolveSuits(suitTable)
                .flatMap(e1 -> among.resolveSuits(e1.suitTable)
                        .map(e2 -> new LongestOrEqual(e1.getSymbol(), e2.getSuitSet()).new Context(e2.suitTable)));
    }

    @Override
    public String toString() {
        return NAME + " " + suit + (among == null ? "" : " among " + among);
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
