package bbidder.parsers;

import static bbidder.Constants.ACE;
import static bbidder.Constants.CHR_ACE;
import static bbidder.Constants.CHR_JACK;
import static bbidder.Constants.CHR_KING;
import static bbidder.Constants.CHR_QUEEN;
import static bbidder.Constants.CHR_TEN;
import static bbidder.Constants.JACK;
import static bbidder.Constants.KING;
import static bbidder.Constants.QUEEN;
import static bbidder.Constants.TEN;

import bbidder.Hand;
import bbidder.utils.BitUtil;
import bbidder.utils.SplitUtil;

public class HandParser {

    public static class ParsedHand {
        public Hand current = new Hand();
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
                    int rank = HandParser.getRank(suitStr.charAt(i), avail.getAllInSuit(suitIndex));
                    avail = avail.withCardEemoved(suitIndex, rank);
                    current = current.withCardAdded(suitIndex, rank);
                }
            }
            while (numX > 0) {
                int rank = HandParser.getRank('X', avail.getAllInSuit(suitIndex));
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

    public static Hand valueOf(String str, Hand avail) {
        if (str == null) {
            return null;
        }
        HandParser.ParsedHand parsed = new HandParser.ParsedHand(avail);
        parsed.parseHand(str);
        return parsed.current;
    }

    public static Hand valueOf(String str) {
        return valueOf(str, Hand.allCards());
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
            return BitUtil.stream(avail).findFirst().get();
        default:
            throw new IllegalArgumentException("Invalid rank: " + cRank);
        }
    }

}
