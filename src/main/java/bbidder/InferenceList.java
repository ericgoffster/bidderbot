package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;

/**
 * Represents a list of inferences read in from the notes.
 *
 * @author goffster
 *
 */
public class InferenceList {
    public final List<Inference> inferences;

    public InferenceList(List<Inference> inferences) {
        super();
        this.inferences = inferences;
    }

    /**
     * @param players
     *            The players
     * @return A bound inference representing all of the inferences as an "and"
     */
    public IBoundInference bind(Players players) {
        IBoundInference result = ConstBoundInference.create(true);
        for (Inference i : inferences) {
            result = AndBoundInf.create(result, i.bind(players));
        }
        return result;
    }
    
    public List<MappedInferenceList> resolveSuits(BiddingContext context) {
        List<MappedInferenceList> list = new ArrayList<>();
        list.add(new MappedInferenceList(new InferenceList(List.of()), context));
        for (Inference i : inferences) {
            List<MappedInferenceList> newList = new ArrayList<>();
            for (MappedInferenceList bi2 : list) {
                for (MappedInf bi : i.resolveSuits(bi2.ctx)) {
                    List<Inference> l = new ArrayList<>(bi2.inf.inferences);
                    l.add(bi.inf);
                    newList.add(new MappedInferenceList(new InferenceList(l), bi.ctx));
                }
            }
            list = newList;
        }
        return list;
    }

    public static InferenceList valueOf(InferenceRegistry registry, String str) {
        if (str == null) {
            return null;
        }
        List<Inference> l = new ArrayList<>();
        for (String part : SplitUtil.split(str, ",")) {
            l.add(registry.valueOf(part));
        }
        return new InferenceList(l);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (Inference inf : inferences) {
            sb.append(delim).append(inf);
            delim = ",";
        }
        return sb.toString();
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
