package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BitUtil;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInference;
import bbidder.SplitUtil;
import bbidder.inferences.bound.SpecificCardsBoundInf;

/**
 * Represents the inference of a specific cards in a suit.
 */
public class SpecificCards implements Inference {
    public final String suit;
    public final short cards;

    public SpecificCards(String suit, short cards) {
        super();
        this.suit = suit;
        this.cards = cards;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        List<MappedInference> l = new ArrayList<>();
        for (var e : context.lookupSuits(suit).entrySet()) {
            l.add(new MappedInference(createBound(e.getKey(), cards), e.getValue()));
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
            short cards = 0;
            for (int i = 0; i < cardsS.length(); i++) {
                char c = cardsS.charAt(i);
                int rank = Hand.getRank(c, (short)0x1ff);
                cards |= 1 << rank;
             }
            return new SpecificCards(suit, cards);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return Hand.printSuit(cards) + " " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards, suit);
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
        return cards == other.cards && Objects.equals(suit, other.suit);
    }

}
