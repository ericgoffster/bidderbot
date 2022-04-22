package bbidder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Inferences happen in the context of a series of bids.
 * This represents those bids in addition to a symbol table of suits.
 * Note this is an immutable class.
 * i.e.
 * 1M 2M (M would get bound to hearts or spades)
 * 
 * @author goffster
 *
 */
public final class BiddingContext {
    public static final BiddingContext EMPTY = new BiddingContext(new BidInference(null, BidPatternList.EMPTY, InferenceList.EMPTY), Map.of());
    private static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
    private final BidInference inf;
    
    private final Map<String, Integer> suits;

    private BiddingContext(BidInference inf, Map<String, Integer> suits) {
        super();
        this.inf = inf;
        this.suits = suits;
    }

    /**
     * @return The list of bids.
     */
    public BidInference getInference() {
        return inf;
    }
    
    public static BiddingContext create(BidInference inf) {
        return new BiddingContext(inf, Map.of());
    }

    /**
     * @return The immutable symbol table.
     */
    public Map<String, Integer> getSuits() {
        return Collections.unmodifiableMap(suits);
    }
    
    public BiddingContext withBidAdded(BidPattern patt) {
        return new BiddingContext(inf.withBidAdded(patt), suits);
    }

    public BiddingContext withInferenceAdded(Inference i) {
        return new BiddingContext(inf.withInferenceAdded(i), suits);
    }

    /**
     * @param symbol
     *            The symbol
     * @return (0,1,2,3,4) for a given symbol, null if not found.
     */
    public Integer getSuit(String symbol) {
        if (symbol.endsWith(":down")) {
            return getSuit(symbol.substring(0, symbol.length() - 5));
        }
        {
            Matcher m = SUIT_PATTERN.matcher(symbol);
            if (m.matches()) {
                String lhs = m.group(1);
                int delta = Integer.parseInt(m.group(2));
                Integer s = getSuit(lhs);
                if (s == null) {
                    return null;
                }
                return (s + 5 - delta) % 5;
            }
        }
        Integer strain = Strain.getStrain(symbol);
        if (strain != null) {
            return strain;
        }
        if (symbol.equals("M") && suits.containsKey("OM")) {
            return otherMajor(suits.get("OM"));
        }
        if (symbol.equals("m") && suits.containsKey("om")) {
            return otherMinor(suits.get("om"));
        }
        if (symbol.equals("OM") && suits.containsKey("M")) {
            return otherMajor(suits.get("M"));
        }
        if (symbol.equals("om") && suits.containsKey("m")) {
            return otherMinor(suits.get("m"));
        }
        return suits.get(symbol);
    }
    
    /**
     * @param suit The suit to match.
     * @return a map of strains to new bidding contexts
     */
    public Map<Integer, BiddingContext> getMappedBiddingContexts(String suit) {
        boolean reverse = false;
        if (suit.endsWith(":down")) {
            reverse = true;
            suit = suit.substring(0, suit.length() - 5);
        }
        {
            Integer strain = getSuit(suit);
            if (strain != null) {
                if (strain < 0 || strain > 4) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return Map.of(strain, this);
            }
        }
        TreeMap<Integer, BiddingContext> m = new TreeMap<>();
        for (int strain : BitUtil.iterate(BidPattern.getSuitClass(suit))) {
            if (!suits.containsValue(strain)) {
                Map<String, Integer> newSuits = new HashMap<>(suits);
                putSuit(newSuits, suit, strain);
                m.put(strain, new BiddingContext(inf, newSuits));
            }
        }
        return reverse ? m.descendingMap() : m;
    }

    /**
     * @param symbol The symbol to test
     * @return true, if the symbol is syntactically a valid suit.
     */
    public static boolean isValidSuit(String symbol) {
        {
            Matcher m = SUIT_PATTERN.matcher(symbol);
            if (m.matches()) {
                String lhs = m.group(1);
                return isValidSuit(lhs);
            }
        }
        if (symbol.endsWith(":down")) {
            return isValidSuit(symbol.substring(0, symbol.length() - 5));
        }
        if (symbol.startsWith("~")) {
            return isValidSuit(symbol.substring(1));
        }
        if (symbol.equals("om")) {
            return true;
        }
        if (symbol.equals("OM")) {
            return true;
        }
        Integer strain = Strain.getStrain(symbol);
        if (strain != null) {
            return true;
        }
        return symbol.length() == 1;
    }

    @Override
    public String toString() {
        if (suits.isEmpty()) {
            return inf.toString();
        }
        return inf + " where " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inf, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BiddingContext other = (BiddingContext) obj;
        return Objects.equals(inf, other.inf) && Objects.equals(suits, other.suits);
    }

    private static void putSuit(Map<String, Integer> suits, String symbol, int strain) {
        Matcher m = SUIT_PATTERN.matcher(symbol);
        if (m.matches()) {
            String lhs = m.group(1);
            int delta = Integer.parseInt(m.group(2));
            putSuit(suits, lhs, (strain + delta) % 5);
            return;
        }
        if (strain < 0 || strain > 4) {
            throw new IllegalArgumentException("Invalid strain");
        }

        if (symbol.endsWith(":down")) {
            throw new IllegalArgumentException();
        }

        suits.put(symbol, strain);
    }

    private static Integer otherMajor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.HEART:
            return Constants.SPADE;
        case Constants.SPADE:
            return Constants.HEART;
        default:
            throw new IllegalArgumentException("invalid major");
        }
    }

    private static Integer otherMinor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.CLUB:
            return Constants.DIAMOND;
        case Constants.DIAMOND:
            return Constants.CLUB;
        default:
            throw new IllegalArgumentException("invalid minor");
        }
    }
}