package bbidder.inferences;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.StopperSet;
import bbidder.SuitSet;
import bbidder.SuitSets;
import bbidder.SuitTable;
import bbidder.inferences.bound.PartialStoppersBoundInf;
import bbidder.inferences.bound.StoppersBoundInf;
import bbidder.utils.BitUtil;
import bbidder.utils.SplitUtil;

/**
 * Represents the inference of the stoppers in a suit set.
 * 
 * @author goffster
 *
 */
public final class StoppersInSuits extends Inference {
    private final SuitSet suits;
    private final boolean partial;

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
    public Stream<Context> resolveSymbols(SuitTable suitTable) {
        return suits.resolveSymbols(suitTable).map(e -> new StoppersInSuits(e.suitSet, partial).new Context(e.suitTable));
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
