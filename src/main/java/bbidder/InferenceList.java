package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InferenceList {
    public final List<Inference> inferences;

    public InferenceList(List<Inference> inferences) {
        super();
        this.inferences = inferences;
    }

    public static InferenceList valueOf(InferenceRegistry registry, String str) {
        List<Inference> l = new ArrayList<>();
        for (String part : str.trim().split(",")) {
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
