package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.inferences.AndInference;
import bbidder.inferences.TrueInference;

/**
 * Represents a list of inferences read in from the notes.
 *
 * @author goffster
 *
 */
public class InferenceList {
    public static final InferenceList EMPTY = new InferenceList(TrueInference.T);
    public final Inference inferences;

    public InferenceList(Inference inferences) {
        super();
        this.inferences = inferences;
    }

    public InferenceList withInferenceAdded(Inference i) {
        return new InferenceList(AndInference.create(inferences, i));
    }

    /**
     * @param players
     *            The players
     * @return A bound inference representing all of the inferences as an "and"
     */
    public IBoundInference bind(Players players) {
        return inferences.bind(players);
    }

    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        return inferences.resolveSymbols(bc);
    }

    public static InferenceList valueOf(InferenceRegistry registry, String str) {
        if (str == null) {
            return null;
        }
        List<Inference> l = new ArrayList<>();
        for (String part : SplitUtil.split(str, ",")) {
            l.add(registry.valueOf(part));
        }
        return new InferenceList(AndInference.create(l));
    }

    @Override
    public String toString() {
        return inferences.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(inferences);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InferenceList other = (InferenceList) obj;
        return Objects.equals(inferences, other.inferences);
    }
}
