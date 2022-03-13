package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.StopperSet;

public class TotalPtsBoundInf implements IBoundInference {
    final InfSummary partnerSummary;
    final Range r;

    @Override
    public IBoundInference negate() {
        return create(partnerSummary, r.not());
    }

    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, r, StopperSet.ALL);
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        if (other instanceof TotalPtsBoundInf) {
            if (partnerSummary.equals(((TotalPtsBoundInf) other).partnerSummary)) {
                return create(partnerSummary, ((TotalPtsBoundInf) other).r.and(r));
            }
        }
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        if (other instanceof TotalPtsBoundInf) {
            if (partnerSummary.equals(((TotalPtsBoundInf) other).partnerSummary)) {
                return create(partnerSummary, ((TotalPtsBoundInf) other).r.or(r));
            }
        }
        return null;
    }

    public static IBoundInference create(InfSummary partnerSummary, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        return new TotalPtsBoundInf(partnerSummary, r);
    }

    private TotalPtsBoundInf(InfSummary tp, Range r) {
        this.partnerSummary = tp;
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        return r.contains(hand.getTotalPoints(partnerSummary));
    }

    @Override
    public String toString() {
        if (partnerSummary.shape.unBounded()) {
            return r + " tpts";
        }
        return r + " tpts" + ", partner=" + partnerSummary.shape;
    }

    @Override
    public int hashCode() {
        return Objects.hash(partnerSummary, r);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TotalPtsBoundInf other = (TotalPtsBoundInf) obj;
        return Objects.equals(partnerSummary, other.partnerSummary) && Objects.equals(r, other.r);
    }
}