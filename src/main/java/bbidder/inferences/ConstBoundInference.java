package bbidder.inferences;

import bbidder.Hand;
import bbidder.IBoundInference;

public class ConstBoundInference implements IBoundInference {
    public final boolean result;

    public ConstBoundInference(boolean result) {
        super();
        this.result = result;
    }

    public boolean matches(Hand hand) {
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(result);
    }
}
