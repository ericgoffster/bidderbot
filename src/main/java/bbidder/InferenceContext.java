package bbidder;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class InferenceContext {
    public final Inference inference;

    private final Map<String, Integer> suits;

    public InferenceContext(Inference inference, Map<String, Integer> suits) {
        super();
        this.inference = inference;
        this.suits = suits;
    }
    
    /**
     * @return The immutable symbol table.
     */
    public Map<String, Integer> getSuits() {
        return Collections.unmodifiableMap(suits);
    }

    @Override
    public String toString() {
        if (suits.isEmpty()) {
            return inference.toString();
        }
        return inference + " where " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inference, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InferenceContext other = (InferenceContext) obj;
        return Objects.equals(inference, other.inference) && Objects.equals(suits, other.suits);
    }
}