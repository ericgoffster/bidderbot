package bbidder.inferences;

import bbidder.Hand;
import bbidder.IBoundInference;

public class OrBoundInference implements IBoundInference {
    public final IBoundInference i1;
    public final IBoundInference i2;

    private OrBoundInference(IBoundInference i1, IBoundInference i2) {
        super();
        this.i1 = i1;
        this.i2 = i2;
    }

    public boolean matches(Hand hand) {
        return i1.matches(hand) || i2.matches(hand);
    }

    @Override
    public String toString() {
        return "(or " + i1 + " " + i2 + ")";
    }
    
    @Override
    public boolean negatable() {
        return i1.negatable() && i2.negatable();
    }
    
    @Override
    public IBoundInference negate() {
        return AndBoundInference.create(i1.negate(), i2.negate());
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        if (i1 == ConstBoundInference.T || i2 == ConstBoundInference.T) {
            return ConstBoundInference.T;
        }
        if (i1 == ConstBoundInference.F) {
            return i2;
        }
        if (i2 == ConstBoundInference.F) {
            return i1;
        }
        return new OrBoundInference(i1, i2);
    }
}
