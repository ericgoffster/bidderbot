package bbidder;

import java.util.ArrayList;
import java.util.List;

import bbidder.inferences.AndInference;

/**
 * Represents a list of inferences read in from the notes.
 *
 * @author goffster
 *
 */
public class InferenceList {
     public static Inference valueOf(InferenceRegistry registry, String str) {
        if (str == null) {
            return null;
        }
        List<Inference> l = new ArrayList<>();
        for (String part : SplitUtil.split(str, ",")) {
            l.add(registry.valueOf(part));
        }
        return AndInference.create(l);
    }
}
