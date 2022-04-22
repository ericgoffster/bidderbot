package bbidder.inferences;

import java.util.List;
import java.util.Map;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Players;
import bbidder.inferences.bound.ConstBoundInference;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public class Always implements Inference {
    public Always() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ConstBoundInference.T;
    }

    @Override
    public List<InferenceContext> resolveSymbols(Map<String, Integer> suits) {
        return List.of(new InferenceContext(this, suits));
    }

    public static Always valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("always")) {
            return new Always();
        }
        return null;
    }

    @Override
    public String toString() {
        return "always";
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
