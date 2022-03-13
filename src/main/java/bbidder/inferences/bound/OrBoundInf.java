package bbidder.inferences.bound;

import java.util.ArrayList;
import java.util.List;

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
        for(IBoundInference i: inferences) {
            s = s.or(i.getSummary());
        }
        return s;
    }

    @Override
    public IBoundInference andWith(InfSummary summary) {
        List<IBoundInference> l = new ArrayList<>();
        for(IBoundInference i : inferences) {
            IBoundInference t = i.andWith(summary);
            if (t == ConstBoundInference.T) {
                return ConstBoundInference.T;
            }
            if (t == ConstBoundInference.F) {
                continue;
            }
            l.add(t);
        }
        return create(l);
    }
    
    @Override
    public IBoundInference negate() {
        List<IBoundInference> l = new ArrayList<>();
        for(IBoundInference i : inferences) {
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
    
    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> orList = new ArrayList<>();
        for (IBoundInference i : inferences) {
            orList.add(i);
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
}
