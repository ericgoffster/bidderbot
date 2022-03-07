package bbidder;

import static bbidder.Constants.*;

/**
 * A bid.
 * 
 * @author goffster
 *
 */
public enum Bid {
    _1C(0, CLUB),
    _1D(0, DIAMOND),
    _1H(0, HEART),
    _1S(0, SPADE),
    _1N(0, NOTRUMP),
    _2C(1, CLUB),
    _2D(1, DIAMOND),
    _2H(1, HEART),
    _2S(1, SPADE),
    _2N(1, NOTRUMP),
    _3C(2, CLUB),
    _3D(2, DIAMOND),
    _3H(2, HEART),
    _3S(2, SPADE),
    _3N(2, NOTRUMP),
    _4C(3, CLUB),
    _4D(3, DIAMOND),
    _4H(3, HEART),
    _4S(3, SPADE),
    _4N(3, NOTRUMP),
    _5C(4, CLUB),
    _5D(4, DIAMOND),
    _5H(4, HEART),
    _5S(4, SPADE),
    _5N(4, NOTRUMP),
    _6C(5, CLUB),
    _6D(5, DIAMOND),
    _6H(5, HEART),
    _6S(5, SPADE),
    _6N(5, NOTRUMP),
    _7C(6, CLUB),
    _7D(6, DIAMOND),
    _7H(6, HEART),
    _7S(6, SPADE),
    _7N(6, NOTRUMP),
    X(2, -1),
    XX(4, -1),
    P(1, -1)
    ;

    public final int strain;
    public final int level;

    /**
     * A suit bid, not X, XX, or P
     * 
     * @return true if this is a suit bid
     */
    public boolean isSuitBid() {
        return strain >= 0;
    }

    @Override
    public String toString() {
        if (this == P) {
            return STR_P;
        }
        if (this == X) {
            return STR_X;
        }
        if (this == XX) {
            return STR_XX;
        }
        return String.valueOf(level + 1) + STR_ALL_SUITS.charAt(strain);
    }

    /**
     * @return The next level up.
     */
    public Bid raise() {
        return valueOf(level + 1, strain);
    }

    /**
     * Parses a bid.
     * 
     * @param str
     *            The string to parse
     * @return The bid
     */
    public static Bid fromStr(String str) {
        if (str == null) {
            return null;
        }
        if (str.equalsIgnoreCase(STR_P)) {
            return P;
        }
        if (str.equalsIgnoreCase(STR_X)) {
            return X;
        }
        if (str.equalsIgnoreCase(STR_XX)) {
            return XX;
        }
        Integer strain = getStrain(str.substring(1));
        if (strain == null) {
            throw new IllegalArgumentException("Invalid strain: '" + str + "'");
        }
        return valueOf(Integer.parseInt(str.substring(0, 1)) - 1, strain.intValue());
    }

    /**
     * Bid from a level and stain.
     * 
     * @param level
     *            The level
     * @param strain
     *            The strain
     * @return a Bid
     */
    public static Bid valueOf(int level, int strain) {
        return ALL_BIDS[strain][level];
    }

    private Bid(int level, int strain) {
        this.strain = strain;
        this.level = level;
    }

    /**
     * Parses a strain
     * 
     * @param str
     *            The string to parse
     * @return The strain, null if not recognized.
     */
    public static Integer getStrain(String str) {
        if (str == null) {
            return null;
        }
        switch (str.toUpperCase()) {
        case STR_CLUB:
            return CLUB;
        case STR_DIAMOND:
            return DIAMOND;
        case STR_HEART:
            return HEART;
        case STR_SPADE:
            return SPADE;
        case STR_NOTRUMP:
            return NOTRUMP;
        default:
            return null;
        }
    }

    private static final Bid[][] ALL_BIDS = { { _1C, _2C, _3C, _4C, _5C, _6C, _7C }, { _1D, _2D, _3D, _4D, _5D, _6D, _7D },
            { _1H, _2H, _3H, _4H, _5H, _6H, _7H }, { _1S, _2S, _3S, _4S, _5S, _6S, _7S }, { _1N, _2N, _3N, _4N, _5N, _6N, _7N }, };
}
