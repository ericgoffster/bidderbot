package bbidder;

import static bbidder.Constants.CLUB;
import static bbidder.Constants.DIAMOND;
import static bbidder.Constants.HEART;
import static bbidder.Constants.NOTRUMP;
import static bbidder.Constants.SPADE;
import static bbidder.Constants.STR_CLUB;
import static bbidder.Constants.STR_DIAMOND;
import static bbidder.Constants.STR_HEART;
import static bbidder.Constants.STR_NOTRUMP;
import static bbidder.Constants.STR_SPADE;

/**
 * Methods for dealing with strains.
 * A strain can be a suit or NOTRUMP
 * 
 * @author goffster
 *
 */
public final class Strain {

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
        case "♣":
            return CLUB;
        case STR_DIAMOND:
        case "♦":
            return DIAMOND;
        case STR_HEART:
        case "♥":
            return HEART;
        case STR_SPADE:
        case "♠":
            return SPADE;
        case STR_NOTRUMP:
            return NOTRUMP;
        default:
            return null;
        }
    }

    /**
     * @param strain
     *            The strain.
     * @return The name associated with the strain.
     */
    public static String getName(Integer strain) {
        if (strain == null) {
            return null;
        }
        return String.valueOf(Constants.STR_ALL_SUITS.charAt(strain));
    }

    /**
     * @param str
     *            The string
     * @return the suit if a string, null if NOTRUMP
     */
    public static Integer getSuit(String str) {
        Integer strain = Strain.getStrain(str);
        if (strain != null && strain.intValue() == NOTRUMP) {
            return null;
        }
        return strain;
    }
}
