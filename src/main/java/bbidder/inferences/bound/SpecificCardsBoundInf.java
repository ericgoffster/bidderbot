package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.StopperSet;

public class SpecificCardsBoundInf implements IBoundInference {
    final Hand specificCards;
    final boolean have;

    @Override
    public IBoundInference negate() {
        return new SpecificCardsBoundInf(specificCards, !have);
    }

    public static IBoundInference create(Hand hand) {
        return new SpecificCardsBoundInf(hand, true);
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        return null;
    }

    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, Range.all(40), StopperSet.ALL);
    }

    private SpecificCardsBoundInf(Hand specificCards, boolean have) {
        this.specificCards = specificCards;
        this.have = have;
    }

    @Override
    public boolean matches(Hand hand) {
        return have ? hand.haveCards(specificCards) : !hand.haveCards(specificCards);
    }

    @Override
    public String toString() {
        return have ? ("have " + specificCards) : ("not have " + specificCards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(have, specificCards);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpecificCardsBoundInf other = (SpecificCardsBoundInf) obj;
        return have == other.have && Objects.equals(specificCards, other.specificCards);
    }
}