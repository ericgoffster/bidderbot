package bbidder;

import static bbidder.Constants.ALL_SUITS;
import static bbidder.Constants.MAJORS;
import static bbidder.Constants.MINORS;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A bidding context is used to build BidInference's.
 * A bidding context consists of the current BidInference, and the current symbol table.
 * A call to resolveSymbols will create "N" different version of the bidding context,
 * each with a different value of the suit that is allowed, added toe the symbol table.
 * 
 * Note that a BiddingContext is immutable.
 * 
 * @author goffster
 *
 */
public final class BiddingContext {
    public static final BiddingContext EMPTY = new BiddingContext(BidInference.EMPTY, Map.of());
    private static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
    private final BidInference bidInference;

    private final Map<String, Integer> suits;

    private BiddingContext(BidInference bidInference, Map<String, Integer> suits) {
        super();
        this.bidInference = bidInference;
        this.suits = suits;
    }

    /**
     * @return The list of bids.
     */
    public BidInference getInference() {
        return bidInference;
    }

    /**
     * @return The immutable symbol table.
     */
    public Map<String, Integer> getSuits() {
        return Collections.unmodifiableMap(suits);
    }

    /**
     * @param patt
     *            The bid pattern to add.
     * @return A new BiddingContext with the given bid added to the bid list.
     */
    public BiddingContext withBidAdded(BidPattern patt) {
        return new BiddingContext(bidInference.withBidAdded(patt), suits);
    }

    /**
     * @param i
     *            An inference
     * @return A new BiddingContext with the given inference added to the inference list.
     */
    public BiddingContext withInferenceAdded(Inference i) {
        return new BiddingContext(bidInference.withInferenceAdded(i), suits);
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
     * 
     * @param suits The suits to rotate
     * @return The suits, rotated down once.
     */
    public static short rotateDown(short suits) {
        return (short) ((suits >> 1) | ((suits & 1) << 4));
    }
    
    public static short getSuitClass(String str) {
        if (str.startsWith("~")) {
            return (short) (0xf ^ getSuitClass(str.substring(1)));
        }
        int pos = str.indexOf("-");
        if (pos >= 0) {
            int n = Integer.parseInt(str.substring(pos + 1));
            short suits = getSuitClass(str.substring(0, pos));
            while (n > 0) {
                suits = rotateDown(suits);
                n--;
            }
            return suits;
        }
        {
            Integer strain = Strain.getStrain(str);
            if (strain != null) {
                if (strain < 0 || strain > 3) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return (short) (1 << strain);
            }
        }

        switch (str) {
        case "M":
            return MAJORS;
        case "OM":
            return MAJORS;
        case "m":
            return MINORS;
        case "om":
            return MINORS;
        default:
            return ALL_SUITS;
        }
    }

    /**
     * @param symbol
     *            The suit to match.
     * @return a map of strains to new bidding contexts. Each key in the map represents a possible bid strain
     *         for the given suit.
     */
    public Map<Integer, BiddingContext> resolveSymbols(String symbol) {
        boolean reverse = false;
        if (symbol.endsWith(":down")) {
            reverse = true;
            symbol = symbol.substring(0, symbol.length() - 5);
        }
        {
            Integer strain = getSuit(symbol);
            if (strain != null) {
                if (strain < 0 || strain > 4) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return Map.of(strain, this);
            }
        }
        TreeMap<Integer, BiddingContext> m = new TreeMap<>();
        for (int strain : BitUtil.iterate(getSuitClass(symbol))) {
            if (!suits.containsValue(strain)) {
                Map<String, Integer> newSuits = new HashMap<>(suits);
                putSuit(newSuits, symbol, strain);
                m.put(strain, new BiddingContext(bidInference, newSuits));
            }
        }
        return reverse ? m.descendingMap() : m;
    }

    /**
     * @param symbol
     *            The symbol to test
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
            return bidInference.toString();
        }
        return bidInference + " where " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bidInference, suits);
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
        return Objects.equals(bidInference, other.bidInference) && Objects.equals(suits, other.suits);
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