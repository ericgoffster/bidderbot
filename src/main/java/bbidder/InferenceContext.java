package bbidder;

import java.util.Collections;
import java.util.Map;

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
}