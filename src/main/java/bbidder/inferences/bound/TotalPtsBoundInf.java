package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.PointRange;

public final class TotalPtsBoundInf implements IBoundInference {
    private final InfSummary partnerSummary;
    private final PointRange r;

    @Override
    public IBoundInference negate() {
        return create(partnerSummary, r.not());
    }

    @Override
    public InfSummary getSummary() {
        return InfSummary.ALL.withTotalPoints(r);
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

    public static IBoundInference create(InfSummary partnerSummary, PointRange r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        return new TotalPtsBoundInf(partnerSummary, r);
    }

    private TotalPtsBoundInf(InfSummary tp, PointRange r) {
        this.partnerSummary = tp;
        this.r = r;
    }

    @Override
    public boolean test(Hand hand) {
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