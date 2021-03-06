package bbidder.inferences;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.utils.MyStream;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public final class TrueInference extends Inference {
    public static final String NAME = "true";
    public static final TrueInference T = new TrueInference();

    private TrueInference() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ConstBoundInference.T;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return NAME;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
