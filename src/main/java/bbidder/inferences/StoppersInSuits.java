package bbidder.inferences;

import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.BitUtil;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInf;
import bbidder.MappedInference;
import bbidder.SplitUtil;
import bbidder.StopperSet;
import bbidder.inferences.bound.PartialStoppersBoundInf;
import bbidder.inferences.bound.StoppersBoundInf;

/**
 * Represents the inference of the stoppers in a suit set.
 * 
 * @author goffster
 *
 */
public class StoppersInSuits implements Inference {
    public final String suits;
    public final boolean partial;

    public StoppersInSuits(String suits, boolean partial) {
        super();
        this.suits = suits;
        this.partial = partial;
    }

    public static Inference valueOf(String str) {
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("stoppers")) {
            return new StoppersInSuits(parts[1], false);
        }
        if (parts.length == 2 && parts[0].equalsIgnoreCase("partial_stoppers")) {
            return new StoppersInSuits(parts[1], true);
        }
        return null;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        short theSuits = context.lookupSuitSet(suits);
        StopperSet stoppers = new StopperSet(stopper -> {
            for (int s : BitUtil.iterate(theSuits)) {
                if (!stopper.stopperIn(s)) {
                    return false;
                }
            }
            return true;
        });
        if (partial) {
            return List.of(new MappedInference(PartialStoppersBoundInf.create(stoppers), context));
        }
        return List.of(new MappedInference(StoppersBoundInf.create(stoppers), context));
    }
    
    @Override
    public List<MappedInf> resolveSuits(BiddingContext context) {
        return List.of(new MappedInf(this, context));
    }

    @Override
    public String toString() {
        return (partial ? "partial_stoppers " : "stoppers ") + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StoppersInSuits other = (StoppersInSuits) obj;
        return Objects.equals(suits, other.suits);
    }
}
