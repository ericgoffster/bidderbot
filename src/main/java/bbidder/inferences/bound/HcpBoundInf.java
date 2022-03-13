package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;

public class HcpBoundInf implements IBoundInference {
    final Range r;

    @Override
    public IBoundInference negate() {
        return create(r.not());
    }

    public static IBoundInference create(Range r) {
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        return new HcpBoundInf(r);
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        if (other instanceof HcpBoundInf) {
            return create(((HcpBoundInf) other).r.and(r));
        }
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        if (other instanceof HcpBoundInf) {
            return create(((HcpBoundInf) other).r.or(r));
        }
        return null;
    }

    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, Range.all(40));
    }

    private HcpBoundInf(Range r) {
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        return r.contains(hand.numHCP());
    }

    @Override
    public String toString() {
        return r + " hcp";
    }

    @Override
    public int hashCode() {
        return Objects.hash(r);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HcpBoundInf other = (HcpBoundInf) obj;
        return Objects.equals(r, other.r);
    }
}