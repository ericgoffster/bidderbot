package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.Constants;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInf;
import bbidder.MappedInference;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public class SuitRange implements Inference {
    public final String suit;
    public final Range rng;

    public SuitRange(String suit, Integer min, Integer max) {
        super();
        this.suit = suit;
        this.rng = Range.between(min, max, 13);
    }

    public SuitRange(String suit, Range r) {
        super();
        this.suit = suit;
        this.rng = r;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        List<MappedInference> l = new ArrayList<>();
        for (var e : context.lookupSuits(suit).entrySet()) {
            l.add(new MappedInference(createBound(e.getKey(), rng), e.getValue()));
        }
        return l;
    }
    
    @Override
    public List<MappedInf> resolveSuits(BiddingContext context) {
        List<MappedInf> l = new ArrayList<>();
        for (var e : context.getMappedBiddingContexts(suit).entrySet()) {
            l.add(new MappedInf(new SuitRange(String.valueOf(Constants.STR_ALL_SUITS.charAt(e.getKey())), rng), e.getValue()));
        }
        return l;
    }

    private static IBoundInference createBound(int s, Range r) {
        return ShapeBoundInf.create(new ShapeSet(shape -> shape.isSuitInRange(s, r)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        RangeOf rng = RangeOf.valueOf(str);
        if (rng == null) {
            return null;
        }
        if (BiddingContext.isValidSuit(rng.of)) {
            return new SuitRange(rng.of, Range.between(rng.min, rng.max, 13));
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return rng + " " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng, suit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SuitRange other = (SuitRange) obj;
        return Objects.equals(rng, other.rng) && Objects.equals(suit, other.suit);
    }
}
