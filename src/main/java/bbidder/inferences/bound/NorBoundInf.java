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
public class NorBoundInf implements IBoundInference {
    public final List<IBoundInference> inferences;

    private NorBoundInf(List<IBoundInference> inf) {
        super();
        this.inferences = inf;
    }
    
    @Override
    public boolean matches(Hand hand) {
        for (IBoundInference i : inferences) {
            if (i.matches(hand)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public InfSummary getSummary() {
        InfSummary s = InfSummary.ALL;
        for(IBoundInference i: inferences) {
            s = s.and(i.getNotSummary());
        }
        return s;
    }
    
    @Override
    public InfSummary getNotSummary() {
        InfSummary s = InfSummary.NONE;
        for(IBoundInference i: inferences) {
            s = s.or(i.getSummary());
        }
        return s;
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
            return ConstBoundInference.T;
        }
        return new NorBoundInf(orList);
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            l.add(i.toString());
        }
        return "~(" + String.join(" | ", l) + ")";
    }
}
