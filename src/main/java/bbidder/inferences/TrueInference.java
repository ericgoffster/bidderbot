package bbidder.inferences;

import java.util.List;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Players;
import bbidder.SymbolTable;
import bbidder.inferences.bound.ConstBoundInference;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public final class TrueInference implements Inference {
    public static final TrueInference T = new TrueInference();

    private TrueInference() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ConstBoundInference.T;
    }

    @Override
    public List<InferenceContext> resolveSymbols(SymbolTable symbols) {
        return List.of(new InferenceContext(this, symbols));
    }

    @Override
    public String toString() {
        return "true";
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
