package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.Constants;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInf;
import bbidder.MappedInference;
import bbidder.Range;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.SplitUtil;
import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.HcpBoundInf;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a premptive hand of varying levels.
 * 
 * @author goffster
 *
 */
public class OpeningPreempt implements Inference {
    private final String suit;
    private final int level;

    public OpeningPreempt(String suit, int level) {
        super();
        this.suit = suit;
        this.level = level;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        List<MappedInference> l = new ArrayList<>();
        for (var e : context.lookupSuits(suit).entrySet()) {
            l.add(new MappedInference(AndBoundInf.create(HcpBoundInf.create(Range.between(5, 10, 40)),
                    ShapeBoundInf.create(new ShapeSet(shape -> isPremptive(e.getKey(), level, shape)))), e.getValue()));
        }
        return l;
    }
    
    @Override
    public List<MappedInf> resolveSuits(BiddingContext context) {
        List<MappedInf> l = new ArrayList<>();
        for (var e : context.getMappedBiddingContexts(suit).entrySet()) {
            l.add(new MappedInf(new OpeningPreempt(String.valueOf(Constants.STR_ALL_SUITS.charAt(e.getKey())), level), e.getValue()));
        }
        return l;
    }

    public static OpeningPreempt valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 3);
        if (parts.length != 3) {
            return null;
        }
        if (!parts[0].equalsIgnoreCase("opening_preempt")) {
            return null;
        }
        if (!BiddingContext.isValidSuit(parts[2])) {
            return null;
        }
        return new OpeningPreempt(parts[2], Integer.parseInt(parts[1]));
    }

    private static boolean isPremptive(int suit, int level, Shape hand) {
        int len = hand.numInSuit(suit);
        switch (level) {
        case 2:
            return len == 6;
        case 3:
            return len == 7;
        case 4:
            return len == 8;
        case 5:
            return len > 8;
        default:
            throw new IllegalStateException("Invalid level");
        }
    }

    @Override
    public String toString() {
        return "opening_preempt " + level + " " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, suit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OpeningPreempt other = (OpeningPreempt) obj;
        return level == other.level && Objects.equals(suit, other.suit);
    }
}
