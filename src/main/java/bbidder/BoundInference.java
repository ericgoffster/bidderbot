package bbidder;

import java.util.List;

public class BoundInference {
    public final List<BoundInference> negative;
    public final InferenceList inferences;
    public final Context context;
    public BoundInference(InferenceList inferences, Context context, List<BoundInference> negative) {
        super();
        this.inferences = inferences;
        this.context = context;
        this.negative = negative;
    }
    
    public boolean matches(Hand hand) {
        for(BoundInference bi: negative) {
            if (bi.matches(hand)) {
                return false;
            }
        }
        return inferences.matches(context, hand);
    }
}
