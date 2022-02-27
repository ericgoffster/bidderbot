package bbidder.inferences;

import java.util.Objects;

import bbidder.Context;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

public class OpeningPreempt implements Inference {
    private final String suit;
    private final int level;

    public static OpeningPreempt valueOf(String str) {
        str = str.trim();
        if (!str.toLowerCase().startsWith("opening_preempt")) {
            return null;
        }
        String[] parts = str.split("\\s+");
        if (parts.length != 3) {
            return null;
        }
        return new OpeningPreempt(parts[2], Integer.parseInt(parts[1]));
    }

    @Override
    public String toString() {
        return "opening_preempt " + level + " " + suit;
    }

    public OpeningPreempt(String suit, int level) {
        super();
        this.suit = suit;
        this.level = level;
    }

    @Override
    public IBoundInference bind(Context context) {
        return new BoundInf(context.lookupSuit(suit), level);
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
        public boolean matches(Hand hand) {
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
            return "opening_preempt " + level + " " + "CDHS".charAt(suit);
        }
    }
}
