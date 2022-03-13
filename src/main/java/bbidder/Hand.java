package bbidder;

import static bbidder.Constants.ACE;
import static bbidder.Constants.CHR_ACE;
import static bbidder.Constants.CHR_JACK;
import static bbidder.Constants.CHR_KING;
import static bbidder.Constants.CHR_QUEEN;
import static bbidder.Constants.CHR_TEN;
import static bbidder.Constants.JACK;
import static bbidder.Constants.KING;
import static bbidder.Constants.NOTRUMP;
import static bbidder.Constants.QUEEN;
import static bbidder.Constants.TEN;

import java.util.Objects;

/**
 * Represents a hand in bridge.
 * 
 * @author goffster
 *
 */
public class Hand {
    private final long cards;

    private Hand(long cards) {
        super();
        this.cards = cards;
    }

    public Hand() {
        this(0);
    }

    public static Hand allCards() {
        return new Hand((1L << 52) - 1);
    }

    public Hand withCardAdded(int suit, int rank) {
        return new Hand(cards | (1L << (13 * suit + rank)));
    }

    public Hand withCardEemoved(int suit, int rank) {
        return new Hand(cards & ~(1L << (13 * suit + rank)));
    }
    
    public boolean haveCards(Hand specificCards) {
        return (specificCards.cards & cards) == specificCards.cards;
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

    public static int getRank(char cRank, short avail) {
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

    public short getAllInSuit(int suit) {
        return (short) ((cards >> (13 * suit)) & 0x1fff);
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

    private static class ParsedHand {
        Hand current = new Hand();
        Hand avail;

        public ParsedHand(Hand avail) {
            super();
            this.avail = avail;
        }

        public void parseSuit(String suitStr, int suitIndex) {
            if (suitStr.equals("-")) {
                return;
            }
            int numX = 0;
            for (int i = 0; i < suitStr.length(); i++) {
                if (suitStr.charAt(i) == 'x' || suitStr.charAt(i) == 'X') {
                    numX++;
                } else {
                    int rank = getRank(suitStr.charAt(i), avail.getAllInSuit(suitIndex));
                    avail = avail.withCardEemoved(suitIndex, rank);
                    current = current.withCardAdded(suitIndex, rank);
                }
            }
            while (numX > 0) {
                int rank = getRank('X', avail.getAllInSuit(suitIndex));
                avail = avail.withCardEemoved(suitIndex, rank);
                current = current.withCardAdded(suitIndex, rank);
                numX--;
            }
        }

        public void parseHand(String str) {
            String[] suit_parts = SplitUtil.split(str, "\\s+", 4);
            if (suit_parts.length != 4) {
                throw new IllegalArgumentException("Expected 4 suits: '" + str + '"');
            }
            int suit = 3;
            for (String suitStr : suit_parts) {
                parseSuit(suitStr, suit--);
            }
        }
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
        return BitUtil.size(getAllInSuit(suit));
    }

    public Shape getShape() {
        return Shape.getShape(numInSuit(0), numInSuit(1), numInSuit(2), numInSuit(3));
    }

    public int size() {
        return numInSuit(0) + numInSuit(1) + numInSuit(2) + numInSuit(3);
    }

    public int pointsInSuit(int suit) {
        int pts = 0;
        for (int rank : BitUtil.iterate(getAllInSuit(suit) & Hand.aboveRank(TEN))) {
            pts += getHCP(rank);
        }
        return pts;
    }

    public int numHCP() {
        int hcp = 0;
        for (int suit = 0; suit < 4; suit++) {
            hcp += pointsInSuit(suit);
        }
        return hcp;
    }

    public boolean haveFit(InfSummary partnerSummary, int suit) {
        return numInSuit(suit) + partnerSummary.getSuit(suit).lowest() >= 8;
    }

    public int getTotalPoints(InfSummary partnerSummary) {
        int pts = totalPoints(Constants.NOTRUMP);
        for (int s = 0; s < 4; s++) {
            if (haveFit(partnerSummary, s)) {
                pts = Math.max(pts, totalPoints(s));
            }
        }
        return pts > 40 ? 40 : pts;
    }

    public int totalPoints(int suit) {
        int pts = numHCP();
        if (suit != NOTRUMP) {
            for (int s = 0; s < 4; s++) {
                if (s != suit) {
                    switch (numInSuit(s)) {
                    case 0:
                        pts += 3;
                        break;
                    case 1:
                        pts += 2;
                        if (pointsInSuit(s) > 0) {
                            pts--;
                        }
                        break;
                    case 2:
                        pts += 1;
                        if (pointsInSuit(s) > 0) {
                            pts--;
                        }
                        break;
                    default:
                        break;
                    }
                }
            }
        }
        return pts;
    }

    public static Hand valueOf(String str, Hand avail) {
        if (str == null) {
            return null;
        }
        ParsedHand parsed = new ParsedHand(avail);
        parsed.parseHand(str);
        return parsed.current;
    }

    public static Hand valueOf(String str) {
        return valueOf(str, Hand.allCards());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (int suit = 3; suit >= 0; suit--) {
            sb.append(delim).append(printSuit(getAllInSuit(suit)));
            delim = " ";
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
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
        return cards == other.cards;
    }
}
