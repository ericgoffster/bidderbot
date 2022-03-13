package bbidder.inferences;

import java.util.Objects;
import static bbidder.Constants.*;

import bbidder.InferenceContext;
import bbidder.SplitUtil;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

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
    public IBoundInference bind(InferenceContext context) {
        return new BoundInf(context.lookupSuit(suit), level);
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
        return new OpeningPreempt(parts[2], Integer.parseInt(parts[1]));
    }

    private static boolean isPremptive(int suit, int level, Hand hand) {
        int hcp = hand.numHCP();
        int len = hand.numInSuit(suit);
        switch (level) {
        case 2:
            return hcp >= 5 && hcp <= 10 && len == 6;
        case 3:
            return hcp >= 5 && hcp <= 10 && len == 7;
        case 4:
            return hcp >= 5 && hcp <= 10 && len == 8;
        case 5:
            return hcp >= 5 && hcp <= 10 && len > 8;
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

    static class BoundInf implements IBoundInference {
        final int suit;
        final int level;

        public BoundInf(int suit, int level) {
            super();
            this.suit = suit;
            this.level = level;
        }

        @Override
        public IBoundInference negate() {
            return new NotBoundInf(suit, level);
        }

        @Override
        public boolean matches(Hand hand) {
            return isPremptive(suit, level, hand);
        }

        @Override
        public String toString() {
            return "opening_preempt " + level + " " + STR_ALL_SUITS.charAt(suit);
        }

        @Override
        public IBoundInference andReduce(IBoundInference i) {
            return null;
        }

        @Override
        public IBoundInference orReduce(IBoundInference i) {
            return null;
        }
    }

    static class NotBoundInf implements IBoundInference {
        final int suit;
        final int level;

        public NotBoundInf(int suit, int level) {
            super();
            this.suit = suit;
            this.level = level;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInf(suit, level);
        }

        @Override
        public boolean matches(Hand hand) {
            return !isPremptive(suit, level, hand);
        }

        @Override
        public String toString() {
            return "non_opening_preempt " + level + " " + STR_ALL_SUITS.charAt(suit);
        }

        @Override
        public IBoundInference andReduce(IBoundInference i) {
            return null;
        }

        @Override
        public IBoundInference orReduce(IBoundInference i) {
            return null;
        }
    }
}
