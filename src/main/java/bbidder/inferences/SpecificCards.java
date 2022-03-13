package bbidder.inferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.BiddingContext;
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
    public final Set<Short> cards;
    
    public static Pattern PATT = Pattern.compile("\\s*(\\d+)\\s+of\\s+top\\s+(\\d+)\\s+in\\s+(.*)\\s*");

    public SpecificCards(String suit, Set<Short> cards) {
        super();
        this.suit = suit;
        this.cards = cards;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        List<MappedInference> l = new ArrayList<>();
        for (var e : context.lookupSuits(suit).entrySet()) {
            List<IBoundInference> orList = new ArrayList<>();
            for (short crd : cards) {
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
    
    public static Set<Short> atLeastOneOf(int n, int top) {
        Set<Short> cards = new HashSet<>();
        for(int i = 0; i < (1L << top); i++) {
            if (BitUtil.size(i) >= n) {
                cards.add((short)(i << (13 - top)));
            }
        }
        return cards;
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = PATT.matcher(str);
        if (m.matches()) {
            int n = Integer.parseInt(m.group(1));
            int top = Integer.parseInt(m.group(2));
            String suit = m.group(3);
            if (!BiddingContext.isValidSuit(suit)) {
                return null;
            }
            Set<Short> s = atLeastOneOf(n, top);
            return new SpecificCards(suit, s);
        }
        String[] parts = SplitUtil.split(str, "\\s+", 3);
        if (parts.length == 3 && parts[1].equalsIgnoreCase("in")) {
            String suit = parts[2];
            if (!BiddingContext.isValidSuit(suit)) {
                return null;
            }
            String cardsS = parts[0].toUpperCase();
            String[] patterns = cardsS.split("\\|");
            Set<Short> cards = new HashSet<>();
            for (int j = 0; j < patterns.length; j++) {
                String patt = patterns[j];
                short crd = 0;
                for (int i = 0; i < patt.length(); i++) {
                    char c = patt.charAt(i);
                    int rank = Hand.getRank(c, (short) 0x1ff);
                    crd |= (short)(1 << rank);
                }
                cards.add(crd);
            }
            return new SpecificCards(suit, cards);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for (short c : cards) {
            l.add(Hand.printSuit(c));
        }
        return String.join("|", l) + " " + suit;
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
        return Objects.equals(cards, other.cards) && Objects.equals(suit, other.suit);
    }
}
