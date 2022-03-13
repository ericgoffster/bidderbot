package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;

public class OrBoundInference implements IBoundInference {
    public final List<IBoundInference> inferences;

    private OrBoundInference(List<IBoundInference> inf) {
        super();
        this.inferences = inf;
    }

    public boolean matches(Hand hand) {
        for(IBoundInference i: inferences) {
            if (i.matches(hand)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean negatable() {
        for(IBoundInference i: inferences) {
            if (!i.negatable()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IBoundInference negate() {
        List<IBoundInference> inf = new ArrayList<>();
        for(IBoundInference i: inferences) {
            inf.add(i.negate());
        }
        return AndBoundInference.create(inf);
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        return create(List.of(i1, i2));
    }
    
    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> l = new ArrayList<>();
        for(IBoundInference i: inferences) {
            if (i == ConstBoundInference.T) {
                return ConstBoundInference.T;
            }
            if (i instanceof OrBoundInference) {
                l.addAll(((OrBoundInference) i).inferences);
            } else if (i != ConstBoundInference.F) {
                l.add(i);
            }
        }
        if (l.size() == 0) {
            return ConstBoundInference.F;
        }
        if (l.size() == 1) {
            return l.get(0);
        }
        return new OrBoundInference(l);
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for(IBoundInference i: inferences) {
            l.add(i.toString());
        }
        return "(" + String.join(" | ", l) + ")";
    }
}
