package bbidder.inferences.bound;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;

/**
 * Represents the "or" of 2 or more bound inferences.
 * 
 * @author goffster
 *
 */
public class OrBoundInf implements IBoundInference {
    public final List<IBoundInference> inferences;

    private OrBoundInf(List<IBoundInference> inf) {
        super();
        this.inferences = inf;
    }

    @Override
    public InfSummary getSummary() {
        InfSummary s = InfSummary.NONE;
        for (IBoundInference i : inferences) {
            s = s.or(i.getSummary());
        }
        return s;
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBoundInference negate() {
        List<IBoundInference> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            l.add(i.negate());
        }
        return AndBoundInf.create(l);
    }

    @Override
    public boolean matches(Hand hand) {
        for (IBoundInference i : inferences) {
            if (i.matches(hand)) {
                return true;
            }
        }
        return false;
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        return create(List.of(i1, i2));
    }

    private static void addOr(List<IBoundInference> l, IBoundInference inf) {
        if (inf == ConstBoundInference.F) {
            return;
        }
        if (inf == ConstBoundInference.T) {
            l.clear();
            l.add(ConstBoundInference.T);
            return;
        }
        if (inf instanceof OrBoundInf) {
            for (IBoundInference i : ((OrBoundInf) inf).inferences) {
                addOr(l, i);
            }
            return;
        }
        for (int i = 0; i < l.size(); i++) {
            IBoundInference comb = inf.orWith(l.get(i));
            if (comb != null) {
                l.remove(i);
                addOr(l, comb);
                return;
            }
        }
        l.add(inf);
    }

    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> orList = new ArrayList<>();
        for (IBoundInference i : inferences) {
            addOr(orList, i);
        }
        if (orList.size() == 0) {
            return ConstBoundInference.F;
        }
        if (orList.size() == 1) {
            return orList.get(0);
        }
        return new OrBoundInf(orList);
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            l.add(i.toString());
        }
        return "(" + String.join(" | ", l) + ")";
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
        OrBoundInf other = (OrBoundInf) obj;
        return Objects.equals(inferences, other.inferences);
    }
}
