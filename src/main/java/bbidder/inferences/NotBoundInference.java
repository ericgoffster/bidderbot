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
    public boolean negatable() {
        return true;
    }

    @Override
    public IBoundInference negate() {
        return inference;
    }

    public static IBoundInference create(IBoundInference i) {
        if (i.negatable()) {
            return i.negate();
        }
        return new NotBoundInference(i);
    }

    @Override
    public String toString() {
        return "(not " + inference + ")";
    }
}
