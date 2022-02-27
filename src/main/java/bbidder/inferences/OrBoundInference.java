package bbidder.inferences;

import bbidder.Hand;
import bbidder.IBoundInference;

public class OrBoundInference implements IBoundInference {
    public final IBoundInference i1;
    public final IBoundInference i2;

    public OrBoundInference(IBoundInference i1, IBoundInference i2) {
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

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        if (i1 instanceof ConstBoundInference) {
            if (!((ConstBoundInference) i1).result) {
                return i2;
            }
            return i1;
        }
        if (i2 instanceof ConstBoundInference) {
            if (!((ConstBoundInference) i2).result) {
                return i1;
            }
            return i2;
        }
        return new OrBoundInference(i1, i2);
    }
}
