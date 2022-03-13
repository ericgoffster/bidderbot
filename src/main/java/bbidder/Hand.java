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

    static int getRank(char cRank, short avail) {
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
        case 'X':
            return BitUtil.iterate(avail).iterator().next();
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

    public static short parseSuit(String suitStr, int suitIndex, Hand avail) {
        if (suitStr.equals("-")) {
            return 0;
        }
        short suit = 0;
        int numX = 0;
        for (int i = 0; i < suitStr.length(); i++) {
            if (suitStr.charAt(i) == 'x' || suitStr.charAt(i) == 'X') {
                numX++;
            } else {
                int rank = getRank(suitStr.charAt(i), avail.suits[suitIndex]);
                avail.suits[suitIndex] &= ~(1 << rank);
                suit |= (1 << rank);
            }
        }
        while(numX > 0) {
            int rank = getRank('X', avail.suits[suitIndex]);
            avail.suits[suitIndex] &= ~(1 << rank);
            suit |= (1 << rank);
            numX--;
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
    
    public int size() {
        return numInSuit(0) + numInSuit(1) + numInSuit(2) + numInSuit(3);
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

    public static Hand valueOf(String str, Hand avail) {
        if (str == null) {
            return null;
        }
        int suit = 3;
        short[] suits = new short[4];
        String[] suit_parts = str.trim().split("\\s+");
        if (suit_parts.length != 4) {
            throw new IllegalArgumentException("Expected 4 suits: '" + str + '"');
        }
        for (String suitStr : suit_parts) {
            short parseSuit = parseSuit(suitStr, suit, avail);
            suits[suit--] = parseSuit;
        }
        return new Hand(suits);
    }

    public static Hand valueOf(String str) {
        return valueOf(str, new Hand(new short[] {0x1fff, 0x1fff, 0x1fff, 0x1fff}));
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
