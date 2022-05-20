package bbidder.inferences.bound;

import java.util.Objects;
import java.util.OptionalInt;

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
        OptionalInt max = r.highest();
        if (!max.isPresent()) {
            return InfSummary.NONE;
        }
        return InfSummary.ALL.withTotalPoints(r).withHcp(PointRange.atMost(max.getAsInt()));
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
        return r + " tpts";
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