package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
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
    public final boolean isFit;

    public static Pattern PATT_FIT = Pattern.compile("\\s*fit\\s*(.*)");
    public static Pattern PATT_MIN_TO_MAX = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(\\d+)\\s*(.*)");
    public static Pattern PATT_MAX = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(.*)");
    public static Pattern PATT_MIN = Pattern.compile("\\s*(\\d+)\\s*\\+\\s*(.*)");
    public static Pattern PATT_EXACT = Pattern.compile("\\s*(\\d+)\\s*(.*)");

    public SuitRange(String suit, Integer min, Integer max) {
        super();
        this.suit = suit;
        this.rng = Range.between(min, max, 13);
        isFit = false;
    }

    public SuitRange(String suit, Range r) {
        super();
        this.suit = suit;
        this.rng = r;
        isFit = false;
    }
    
    public SuitRange(String suit) {
        super();
        this.suit = suit;
        this.rng = null;
        isFit = true;
    }

    @Override
    public List<IBoundInference> bind(InferenceContext context) {
        List<IBoundInference> l = new ArrayList<>();
        for(int isuit: context.lookupSuits(suit).keySet()) {
            if (isFit) {
                l.add(createBound(isuit, Range.atLeast(8 - context.likelyHands.partner.getSuit(isuit).lowest(), 13)));
            } else {
                l.add(createBound(isuit, rng));
            }
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
        Matcher m = PATT_FIT.matcher(str);
        if (m.matches()) {
            return new SuitRange(m.group(1).trim());
        }
        final String suit;
        final Integer min;
        final Integer max;
        m = PATT_MIN_TO_MAX.matcher(str);
        if (m.matches()) {
            suit = m.group(3).trim();
            min = Integer.parseInt(m.group(1));
            max = Integer.parseInt(m.group(2));
        } else {
            m = PATT_MAX.matcher(str);
            if (m.matches()) {
                suit = m.group(2).trim();
                min = null;
                max = Integer.parseInt(m.group(1));
            } else {
                m = PATT_MIN.matcher(str);
                if (m.matches()) {
                    suit = m.group(2).trim();
                    min = Integer.parseInt(m.group(1));
                    max = null;
                } else {
                    m = PATT_EXACT.matcher(str);
                    if (m.matches()) {
                        suit = m.group(2).trim();
                        min = max = Integer.parseInt(m.group(1));
                    } else {
                        return null;
                    }
                }
            }
        }
        if (suit.equalsIgnoreCase("hcp")) {
            return new HCPRange(Range.between(min, max, 40));
        } else if (suit.equalsIgnoreCase("tpts")) {
            return new TotalPointsRange(Range.between(min, max, 40));
        } else {
            return new SuitRange(suit, Range.between(min, max, 13));
        }
    }

    @Override
    public String toString() {
        return isFit ? ("fit "+ suit) : (rng + " " + suit);
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
