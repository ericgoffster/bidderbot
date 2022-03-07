package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;

public class OrBoundInference implements IBoundInference {
    public final IBoundInference i1;
    public final IBoundInference i2;

    private OrBoundInference(IBoundInference i1, IBoundInference i2) {
        super();
        this.i1 = i1;
        this.i2 = i2;
    }

    public boolean matches(Hand hand) {
        return i1.matches(hand) || i2.matches(hand);
    }

    void gatherOrs(List<String> l) {
        if (i1 instanceof OrBoundInference) {
            ((OrBoundInference) i1).gatherOrs(l);
        } else {
            l.add(i1.toString());
        }
        if (i2 instanceof OrBoundInference) {
            ((OrBoundInference) i2).gatherOrs(l);
        } else {
            l.add(i2.toString());
        }
    }

    @Override
    public boolean negatable() {
        return i1.negatable() && i2.negatable();
    }

    @Override
    public IBoundInference negate() {
        return AndBoundInference.create(i1.negate(), i2.negate());
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        if (i1 == ConstBoundInference.T || i2 == ConstBoundInference.T) {
            return ConstBoundInference.T;
        }
        if (i1 == ConstBoundInference.F) {
            return i2;
        }
        if (i2 == ConstBoundInference.F) {
            return i1;
        }
        return new OrBoundInference(i1, i2);
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        gatherOrs(l);
        return "(" + String.join(" | ", l) + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(i1, i2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrBoundInference other = (OrBoundInference) obj;
        return Objects.equals(i1, other.i1) && Objects.equals(i2, other.i2);
    }
}
