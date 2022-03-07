package bbidder;

import java.util.Arrays;
import static bbidder.Constants.*;

/**
 * Represents a hand in bridge.
 * 
 * @author goffster
 *
 */
public class Hand {
    public final short[] suits;

    public Hand(short[] suits) {
        super();
        this.suits = suits;
    }

    static char toRank(int rank) {
        switch (rank) {
        case ACE:
            return CHR_ACE;
        case KING:
            return CHR_KING;
        case QUEEN:
            return CHR_QUEEN;
        case JACK:
            return CHR_JACK;
        case TEN:
            return CHR_TEN;
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
            return (char) (rank + '2');
        default:
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
    }

    static int getRank(char cRank) {
        switch (Character.toUpperCase(cRank)) {
        case CHR_ACE:
            return ACE;
        case CHR_KING:
            return KING;
        case CHR_QUEEN:
            return QUEEN;
        case CHR_JACK:
            return JACK;
        case CHR_TEN:
            return TEN;
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            return cRank - '2';
        default:
            throw new IllegalArgumentException("Invalid rank: " + cRank);
        }
    }

    static int getHCP(int rank) {
        switch (rank) {
        case ACE:
            return 4;
        case KING:
            return 3;
        case QUEEN:
            return 2;
        case JACK:
            return 1;
        default:
            return 0;
        }
    }

    public static String printSuit(short suit) {
        if (BitUtil.size(suit) == 0) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (int rank : BitUtil.iterate(suit)) {
            sb.append(toRank(rank));
        }
        sb.reverse();
        return sb.toString();
    }

    public static short parseSuit(String suitStr) {
        if (suitStr.equals("-")) {
            return 0;
        }
        short suit = 0;
        for (int i = 0; i < suitStr.length(); i++) {
            int rank = getRank(suitStr.charAt(i));
            suit |= (1 << rank);
        }
        return suit;
    }

    public static short belowSuit(int suit) {
        return (short) ((1 << suit) - 1);
    }

    public static short aboveSuit(int suit) {
        return (short) (0xf & ~belowSuit(suit + 1));
    }

    public static short belowRank(int rank) {
        return (short) ((1 << rank) - 1);
    }

    public static short aboveRank(int rank) {
        return (short) (0x1fff & ~belowRank(rank + 1));
    }

    public int numInSuit(int suit) {
        return BitUtil.size(suits[suit]);
    }

    public int numHCP() {
        int hcp = 0;
        for (int suit = 0; suit < 4; suit++) {
            for (int rank : BitUtil.iterate(suits[suit] & Hand.aboveRank(TEN))) {
                hcp += getHCP(rank);
            }
        }
        return hcp;
    }

    public static Hand valueOf(String s) {
        int suit = 3;
        short[] suits = new short[4];
        for (String suitStr : s.trim().split("\\s+")) {
            suits[suit--] = parseSuit(suitStr);
        }
        return new Hand(suits);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (int suit = 3; suit >= 0; suit--) {
            sb.append(delim).append(printSuit(suits[suit]));
            delim = " ";
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(suits);
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
        Hand other = (Hand) obj;
        return Arrays.equals(suits, other.suits);
    }
}
