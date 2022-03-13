package bbidder.inferences.bound;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;

/**
 * Represents the "and" of 2 or more bound inferences
 * 
 * @author goffster
 *
 */
public class AndBoundInf implements IBoundInference {
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
        for(IBoundInference i: inferences) {
            s = s.and(i.getSummary());
        }
        return s;
    }
        
    @Override
    public IBoundInference negate() {
        List<IBoundInference> l = new ArrayList<>();
        for(IBoundInference i : inferences) {
            l.add(i.negate());
        }
        return OrBoundInf.create(l);
    }
    
    @Override
    public IBoundInference andWith(InfSummary summary) {
        List<IBoundInference> l = new ArrayList<>();
        for(IBoundInference i : inferences) {
            IBoundInference t = i.andWith(summary);
            if (t == ConstBoundInference.F) {
                return ConstBoundInference.F;
            }
            if (t == ConstBoundInference.T) {
                continue;
            }
            l.add(t);
        }
        return create(l);
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        if (i1 == ConstBoundInference.T) {
            return i2;
        }
        if (i2 == ConstBoundInference.T) {
            return i1;
        }
        if (i1 == ConstBoundInference.F || i2 == ConstBoundInference.F) {
            return ConstBoundInference.F;
        }
        return create(List.of(i1, i2));
    }
    
    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> andList = new ArrayList<>();
        for (IBoundInference i : inferences) {
            andList.add(i);
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
}
