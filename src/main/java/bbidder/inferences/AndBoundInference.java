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
    
    /**
     * Adds a an inference to a list of inferences being anded together.
     * @param andList The list of inferences being and'ed together.
     * @param rhs inference to add to this list.
     * @return false If any of the inferences is "F"
     */
    public static boolean addAnd(List<IBoundInference> andList, IBoundInference rhs) {
        // One bad apple spoils the rest
        if (rhs == ConstBoundInference.F) {
            return false;
        } else if (rhs != ConstBoundInference.T) {
            // Attempt to combine the inference with an existing inference.
            for(int i = 0; i < andList.size(); i++) {
                IBoundInference lhs = andList.get(i);
                IBoundInference comb = lhs.andReduce(rhs);
                if (comb == null) {
                    comb = rhs.andReduce(lhs);
                }
                // Found a combination, remove original, add the combination.
                if (comb != null) {
                    // Remove 
                    andList.remove(i);
                    return addAnd(andList, comb);
                }
            }
            // Just add to the end
            andList.add(rhs);
            return true;
        } else {
            return true;
        }
    }

    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> andList = new ArrayList<>();
        for (IBoundInference i : inferences) {
            if (!addAnd(andList, i)) {
                return ConstBoundInference.F;
            }
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
}
