package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;

public class AndBoundInference implements IBoundInference {
    public final IBoundInference i1;
    public final IBoundInference i2;

    private AndBoundInference(IBoundInference i1, IBoundInference i2) {
        super();
        this.i1 = i1;
        this.i2 = i2;
    }

    public boolean matches(Hand hand) {
        return i1.matches(hand) && i2.matches(hand);
    }
    
    @Override
    public boolean negatable() {
        return i1.negatable() && i2.negatable();
    }
    
    @Override
    public IBoundInference negate() {
        return OrBoundInference.create(i1.negate(), i2.negate());
    }
    
    void gatherAnds(List<String> l) {
        if (i1 instanceof AndBoundInference) {
            ((AndBoundInference) i1).gatherAnds(l);
        } else {
            l.add(i1.toString());
        }
        if (i2 instanceof AndBoundInference) {
            ((AndBoundInference) i2).gatherAnds(l);
        } else {
            l.add(i2.toString());
        }
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        gatherAnds(l);
        return "(" + String.join(",", l) + ")";
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        if (i1 == ConstBoundInference.F || i2 == ConstBoundInference.F) {
            return ConstBoundInference.F;
        }
        if (i1 == ConstBoundInference.T) {
            return i2;
        }
        if (i2 == ConstBoundInference.T) {
            return i1;
        }
        return new AndBoundInference(i1, i2);
    }
}
