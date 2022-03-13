package bbidder.inferences.bound;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.inferences.OrBoundInf;

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
    public IBoundInference negate() {
        List<IBoundInference> inf = new ArrayList<>();
        for (IBoundInference i : inferences) {
            inf.add(i.negate());
        }
        return OrBoundInf.create(inf);
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
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
