package bbidder.inferences.bound;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;

/**
 * Represents the "and" of 2 or more bound inferences
 * 
 * @author goffster
 *
 */
public final class AndBoundInf implements IBoundInference {
    public final List<IBoundInference> inferences;

    private AndBoundInf(List<IBoundInference> inf) {
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
    public InfSummary getSummary() {
        InfSummary s = InfSummary.ALL;
        for (IBoundInference i : inferences) {
            s = s.and(i.getSummary());
        }
        return s;
    }

    @Override
    public IBoundInference negate() {
        List<IBoundInference> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            l.add(i.negate());
        }
        return OrBoundInf.create(l);
    }

    private static void addAnd(List<IBoundInference> l, IBoundInference inf) {
        if (inf == ConstBoundInference.T) {
            return;
        }
        if (inf == ConstBoundInference.F) {
            l.clear();
            l.add(ConstBoundInference.F);
            return;
        }
        if (inf instanceof AndBoundInf) {
            for (IBoundInference i : ((AndBoundInf) inf).inferences) {
                addAnd(l, i);
            }
            return;
        }
        for (int i = 0; i < l.size(); i++) {
            IBoundInference comb = inf.andWith(l.get(i));
            if (comb != null) {
                l.remove(i);
                addAnd(l, comb);
                return;
            }
        }
        l.add(inf);
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        return null;
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        return create(List.of(i1, i2));
    }

    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> andList = new ArrayList<>();
        for (IBoundInference i : inferences) {
            addAnd(andList, i);
        }
        if (andList.size() == 0) {
            return ConstBoundInference.T;
        }
        if (andList.size() == 1) {
            return andList.get(0);
        }
        return new AndBoundInf(andList);
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            l.add(i.toString());
        }
        return "(" + String.join(" & ", l) + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(inferences);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AndBoundInf other = (AndBoundInf) obj;
        return Objects.equals(inferences, other.inferences);
    }
}
