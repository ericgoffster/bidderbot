package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;

/**
 * Represents the "or" of 2 or more bound inferences.
 * 
 * @author goffster
 *
 */
public class OrBoundInf implements IBoundInference {
    public final List<IBoundInference> inferences;
    public final int size;

    private OrBoundInf(List<IBoundInference> inf) {
        super();
        this.inferences = inf;
        int sz = 0;
        for(IBoundInference i: inf) {
            sz += i.size();
        }
        size = sz + inf.size();
    }
    
    @Override
    public int size() {
        return size;
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

    @Override
    public IBoundInference negate() {
        List<IBoundInference> inf = new ArrayList<>();
        for (IBoundInference i : inferences) {
            inf.add(i.negate());
        }
        return AndBoundInf.create(inf);
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        return create(List.of(i1, i2));
    }
    
    public static void addOr(List<IBoundInference> orList, IBoundInference rhs) {
        // Attempt to combine the inference with an existing inference.
        again: for(;;) {
            for(int i = 0; i < orList.size(); i++) {
                IBoundInference comb = OrBoundInf.orReduce(rhs, orList.get(i));
                // Found a combination, remove original, add the combination.
                if (comb != null) {
                    // Remove 
                    orList.remove(i);
                    rhs = comb;
                    continue again;
                }
            }
            // Just add to the end
            orList.add(rhs);
            return;
        }
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
    public IBoundInference andReduce(IBoundInference i) {
        // (a + b) * c = ab + bc
        List<IBoundInference> result = new ArrayList<>();
        for(IBoundInference j: inferences) {
            result.add(AndBoundInf.create(j, i));
        }
        IBoundInference res = OrBoundInf.create(result);
        if (res.size() < size + i.size() + 1) {
            return res;
        }

        return null;
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        // (a + b) + (c + d) = (a + b + c + d)
        if (i instanceof OrBoundInf) {
            List<IBoundInference> orList = new ArrayList<>(inferences);
            orList.addAll(((OrBoundInf) i).inferences);
            return create(orList);
        }
        return null;
    }

    public static IBoundInference orReduce(IBoundInference a, IBoundInference b) {
        if (a == ConstBoundInference.T || b == ConstBoundInference.T) {
            return ConstBoundInference.T;
        }
        if (a == ConstBoundInference.F) {
            return b;
        }
        if (b == ConstBoundInference.F) {
            return a;
        }
        IBoundInference comb = a.orReduce(b);
        if (comb == null) {
            return b.orReduce(a);
        }
        return comb;
    }
}