package bbidder.inferences;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(inference);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NotBoundInference other = (NotBoundInference) obj;
        return Objects.equals(inference, other.inference);
    }
    
}
