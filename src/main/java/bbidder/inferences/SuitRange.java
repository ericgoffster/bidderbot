package bbidder.inferences;

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
    
    public static Pattern PATT1 = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(\\d+)\\s*(.*)");
    public static Pattern PATT2 = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(.*)");
    public static Pattern PATT3 = Pattern.compile("\\s*(\\d+)\\s*\\+\\s*(.*)");
    public static Pattern PATT4 = Pattern.compile("\\s*(\\d+)\\s*(.*)");

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
    public IBoundInference bind(InferenceContext context) {
        return createBound(context.lookupSuit(suit), rng);
    }

    private static IBoundInference createBound(int s, Range r) {
        return ShapeBoundInf.create(new ShapeSet(shape -> shape.isSuitInRange(s, r)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        final String suit;
        final Integer min;
        final Integer max;
        Matcher m = PATT1.matcher(str);
        if (m.matches()) {
            suit = m.group(3).trim();
            min = Integer.parseInt(m.group(1));
            max = Integer.parseInt(m.group(2));
        } else {
            m = PATT2.matcher(str);
            if (m.matches()) {
                suit = m.group(2).trim();
                min = null;
                max = Integer.parseInt(m.group(1));
            } else {
                m = PATT3.matcher(str);
                if (m.matches()) {
                    suit = m.group(2).trim();
                    min = Integer.parseInt(m.group(1));
                    max = null;
                } else {
                    m = PATT4.matcher(str);
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
        } else {
            return new SuitRange(suit, Range.between(min, max, 13));
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
