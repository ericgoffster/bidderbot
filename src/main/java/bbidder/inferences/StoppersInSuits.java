package bbidder.inferences;

import java.util.List;
import java.util.Objects;

import bbidder.BitUtil;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Players;
import bbidder.SplitUtil;
import bbidder.StopperSet;
import bbidder.SuitSet;
import bbidder.SuitSets;
import bbidder.SymbolTable;
import bbidder.inferences.bound.PartialStoppersBoundInf;
import bbidder.inferences.bound.StoppersBoundInf;

/**
 * Represents the inference of the stoppers in a suit set.
 * 
 * @author goffster
 *
 */
public final class StoppersInSuits implements Inference {
    public final SuitSet suits;
    public final boolean partial;

    public StoppersInSuits(SuitSet suits, boolean partial) {
        super();
        this.suits = suits;
        this.partial = partial;
    }

    public static Inference valueOf(String str) {
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("stoppers")) {
            return new StoppersInSuits(SuitSets.lookupSuitSet(parts[1]), false);
        }
        if (parts.length == 2 && parts[0].equalsIgnoreCase("partial_stoppers")) {
            return new StoppersInSuits(SuitSets.lookupSuitSet(parts[1]), true);
        }
        return null;
    }

    @Override
    public IBoundInference bind(Players players) {
        short theSuits = suits.evaluate(players);
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
    public List<InferenceContext> resolveSymbols(SymbolTable insuits) {
        return List.of(new InferenceContext(new StoppersInSuits(suits.replaceVars(insuits), partial), insuits));
    }

    @Override
    public String toString() {
        return (partial ? "partial_stoppers " : "stoppers ") + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(partial, suits);
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
        return partial == other.partial && Objects.equals(suits, other.suits);
    }
}
