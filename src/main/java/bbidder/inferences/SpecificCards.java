package bbidder.inferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import bbidder.BitUtil;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInference;
import bbidder.SplitUtil;
import bbidder.inferences.bound.OrBoundInf;
import bbidder.inferences.bound.SpecificCardsBoundInf;

/**
 * Represents the inference of a specific cards in a suit.
 */
public class SpecificCards implements Inference {
    public final String suit;
    public final short[] cards;

    public SpecificCards(String suit, short[] cards) {
        super();
        this.suit = suit;
        this.cards = cards;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        List<MappedInference> l = new ArrayList<>();
        for (var e : context.lookupSuits(suit).entrySet()) {
            List<IBoundInference> orList = new ArrayList<>();
            for(short crd: cards) {
                orList.add(createBound(e.getKey(), crd));
            }
            l.add(new MappedInference(OrBoundInf.create(orList), e.getValue()));
        }
        return l;
    }

    private static IBoundInference createBound(int s, short cards) {
        Hand hand = new Hand();
        for (int r : BitUtil.iterate(cards)) {
            hand = hand.withCardAdded(s, r);
        }
        return SpecificCardsBoundInf.create(hand);
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 3);
        if (parts.length == 3 && parts[1].equalsIgnoreCase("in")) {
            String suit = parts[2];
            String cardsS = parts[0].toUpperCase();
            String[] patterns = cardsS.split("\\|");
            short[] cards = new short[patterns.length];
            for(int j = 0; j < patterns.length; j++) {
                String patt = patterns[j];
                for (int i = 0; i < patt.length(); i++) {
                    char c = patt.charAt(i);
                    int rank = Hand.getRank(c, (short)0x1ff);
                    cards[j] |= 1 << rank;
                }

            }
            return new SpecificCards(suit, cards);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for(short c: cards) {
            l.add(Hand.printSuit(c));
        }
        return String.join("|", l) + " " + suit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(cards);
        result = prime * result + Objects.hash(suit);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpecificCards other = (SpecificCards) obj;
        return Arrays.equals(cards, other.cards) && Objects.equals(suit, other.suit);
    }
}
