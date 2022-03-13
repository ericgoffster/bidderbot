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
     * @param context
     *            The context for binding
     * @return A bound inference representing all of the inferences as an "and"
     */
    public List<MappedInference> bind(InferenceContext context) {
        List<MappedInference> list = new ArrayList<>();
        list.add(new MappedInference(ConstBoundInference.create(true), context));
        for (Inference i : inferences) {
            List<MappedInference> newList = new ArrayList<>();
            for(MappedInference bi2: list) {
                for(MappedInference bi: i.bind(bi2.ctx)) {
                    newList.add(new MappedInference(AndBoundInf.create(bi2.inf,bi.inf), bi.ctx));
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
