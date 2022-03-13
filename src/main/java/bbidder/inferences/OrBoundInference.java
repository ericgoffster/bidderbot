package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;

/**
 * Represents the "or" of 2 or more bound inferences.
 * 
 * @author goffster
 *
 */
public class OrBoundInference implements IBoundInference {
    public final List<IBoundInference> inferences;

    private OrBoundInference(List<IBoundInference> inf) {
        super();
        this.inferences = inf;
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
        return AndBoundInference.create(inf);
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        return create(List.of(i1, i2));
    }
    
    public static void addOr(List<IBoundInference> orList, IBoundInference rhs) {
        // Attempt to combine the inference with an existing inference.
        for(int i = 0; i < orList.size(); i++) {
            IBoundInference lhs = orList.get(i);
            IBoundInference comb = InfUtil.orReduce(rhs, lhs);
            // Found a combination, remove original, add the combination.
            if (comb != null) {
                // Remove 
                orList.remove(i);
                addOr(orList, comb);
                return;
            }
        }
        // Just add to the end
        orList.add(rhs);
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
        return new OrBoundInference(orList);
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
        return null;
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        // (a + b) + (c + d) = (a + b + c + d)
        if (i instanceof OrBoundInference) {
            List<IBoundInference> orList = new ArrayList<>(inferences);
            orList.addAll(((OrBoundInference) i).inferences);
            return create(orList);
        }
        return null;
    }
}
