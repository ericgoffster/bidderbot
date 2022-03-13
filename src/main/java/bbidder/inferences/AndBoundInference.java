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
    
    public static void addAnd(List<IBoundInference> andList, IBoundInference rhs) {
        again: for(;;) {
            // Attempt to combine the inference with an existing inference.
            for(int i = 0; i < andList.size(); i++) {
                IBoundInference comb = AndBoundInference.andReduce(andList.get(i), rhs);
                // Found a combination, remove original, add the combination.
                if (comb != null) {
                    // Remove 
                    andList.remove(i);
                    rhs = comb;
                    continue again;
                }
            }
            // Just add to the end
            andList.add(rhs);
            return;
        }
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
        return new AndBoundInference(andList);
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
        // ab * cd = (abcd)
        if (i instanceof AndBoundInference) {
            List<IBoundInference> andList = new ArrayList<>(inferences);
            andList.addAll(((AndBoundInference) i).inferences);
            return create(andList);
        }
        // (ab) * (c + d) = (abc + abd)
        if (i instanceof OrBoundInference) {
            List<IBoundInference> result = new ArrayList<>();
            for(IBoundInference j: ((OrBoundInference) i).inferences) {
                List<IBoundInference> i2 = new ArrayList<IBoundInference>(inferences);
                i2.add(j);
                result.add(create(i2));
            }
            IBoundInference res = OrBoundInference.create(result);
            return res;
        }
        return null;
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        return null;
    }

    public static IBoundInference andReduce(IBoundInference a, IBoundInference b) {
        if (a == ConstBoundInference.F || b == ConstBoundInference.F) {
            return ConstBoundInference.F;
        }
        if (a == ConstBoundInference.T) {
            return b;
        }
        if (b == ConstBoundInference.T) {
            return a;
        }
        IBoundInference comb = a.andReduce(b);
        if (comb == null) {
            return b.andReduce(a);
        }
        return comb;
    }
}
