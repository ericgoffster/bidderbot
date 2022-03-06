package bbidder.inferences;

import bbidder.Hand;
import bbidder.IBoundInference;

public class NotBoundInference implements IBoundInference {
    public final IBoundInference inference;

    private NotBoundInference(IBoundInference inference) {
        super();
        this.inference = inference;
    }

    public boolean matches(Hand hand) {
        return !inference.matches(hand);
    }

    @Override
    public String toString() {
        return "(not " + inference + ")";
    }

    public static IBoundInference create(IBoundInference i) {
        if (i == ConstBoundInference.T) {
            return ConstBoundInference.F;
        }
        if (i == ConstBoundInference.F) {
            return ConstBoundInference.T;
        }
        if (i instanceof NotBoundInference) {
            return ((NotBoundInference) i).inference;
        }
        return new NotBoundInference(i);
    }
}
