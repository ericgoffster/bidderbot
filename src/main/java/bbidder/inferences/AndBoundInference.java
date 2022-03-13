package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;

/**
 * Represents the "and" of 2 or more bound inferences
 * 
 * @author goffster
 *
 */
public class AndBoundInference implements IBoundInference {
    public final List<IBoundInference> inferences;

    private AndBoundInference(List<IBoundInference> inf) {
        super();
        this.inferences = inf;
    }

    @Override
    public boolean matches(Hand hand) {
        for (IBoundInference i : inferences) {
            if (!i.matches(hand)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IBoundInference negate() {
        List<IBoundInference> inf = new ArrayList<>();
        for (IBoundInference i : inferences) {
            inf.add(i.negate());
        }
        return OrBoundInference.create(inf);
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        return create(List.of(i1, i2));
    }

    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            if (i == ConstBoundInference.F) {
                return ConstBoundInference.F;
            }
            if (i instanceof AndBoundInference) {
                l.addAll(((AndBoundInference) i).inferences);
            } else if (i != ConstBoundInference.T) {
                l.add(i);
            }
        }
        if (l.size() == 0) {
            return ConstBoundInference.T;
        }
        if (l.size() == 1) {
            return l.get(0);
        }
        return new AndBoundInference(l);
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            l.add(i.toString());
        }
        return "(" + String.join(" & ", l) + ")";
    }
}
