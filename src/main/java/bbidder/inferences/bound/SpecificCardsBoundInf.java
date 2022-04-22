package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.NOfTop;

public final class SpecificCardsBoundInf implements IBoundInference {
    final NOfTop spec;
    final boolean have;

    @Override
    public IBoundInference negate() {
        return new SpecificCardsBoundInf(spec, !have);
    }

    public static IBoundInference create(NOfTop spec) {
        if (spec.isEmpty()) {
            return ConstBoundInference.F;
        }
        if (spec.isFull()) {
            return ConstBoundInference.T;
        }
        return new SpecificCardsBoundInf(spec, true);
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
        return InfSummary.ALL;
    }

    private SpecificCardsBoundInf(NOfTop spec, boolean have) {
        super();
        this.spec = spec;
        this.have = have;
    }

    @Override
    public boolean matches(Hand hand) {
        return have ? hand.haveCards(spec) : !hand.haveCards(spec);
    }

    @Override
    public String toString() {
        return have ? ("have " + spec) : ("not have " + spec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(have, spec);
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
        return have == other.have && Objects.equals(spec, other.spec);
    }
}