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
    
    public static boolean addAnd(List<IBoundInference> l, IBoundInference rhs) {
        if (rhs == ConstBoundInference.F) {
            return false;
        } else if (rhs != ConstBoundInference.T) {
            for(int i = 0; i < l.size(); i++) {
                IBoundInference lhs = l.get(i);
                IBoundInference comb = lhs.andReduce(rhs);
                if (comb == null) {
                    comb = rhs.andReduce(lhs);
                }
                if (comb != null) {
                    l.remove(i);
                    return addAnd(l, comb);
                }
            }
            l.add(rhs);
            return true;
        } else {
            return true;
        }
    }

    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            if (i instanceof AndBoundInference) {
                for(IBoundInference bi: ((AndBoundInference) i).inferences) {
                    if (!addAnd(l, bi)) {
                        return ConstBoundInference.F;
                    }
                }
            } else {
                if (!addAnd(l, i)) {
                    return ConstBoundInference.F;
                }
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

    @Override
    public IBoundInference andReduce(IBoundInference i) {
        return null;
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        return null;
    }
}
