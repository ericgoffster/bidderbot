package bbidder.inferences;

import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.BitUtil;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.InferenceContext.SuitSet;
import bbidder.MappedInf;
import bbidder.Players;
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
    public final SuitSet suits;
    public final boolean partial;
    public final BiddingContext ctx;

    public StoppersInSuits(SuitSet suits, boolean partial, BiddingContext ctx) {
        super();
        this.suits = suits;
        this.partial = partial;
        this.ctx = ctx;
    }

    public static Inference valueOf(String str) {
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("stoppers")) {
            return new StoppersInSuits(InferenceContext.lookupSuitSet(parts[1]), false, null);
        }
        if (parts.length == 2 && parts[0].equalsIgnoreCase("partial_stoppers")) {
            return new StoppersInSuits(InferenceContext.lookupSuitSet(parts[1]), true, null);
        }
        return null;
    }

    @Override
    public IBoundInference bind(Players players) {
        InferenceContext context = new InferenceContext(players, ctx);
        short theSuits = suits.evaluate(context);
        StopperSet stoppers = new StopperSet(stopper -> {
            for (int s : BitUtil.iterate(theSuits)) {
                if (!stopper.stopperIn(s)) {
                    return false;
                }
            }
            return true;
        });
        if (partial) {
            return PartialStoppersBoundInf.create(stoppers);
        }
        return StoppersBoundInf.create(stoppers);
    }
    
    @Override
    public List<MappedInf> resolveSuits(BiddingContext context) {
        return List.of(new MappedInf(new StoppersInSuits(suits, partial, context), context));
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
