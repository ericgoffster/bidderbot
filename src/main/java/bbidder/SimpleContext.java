package bbidder;

import static bbidder.Constants.CLUB;
import static bbidder.Constants.DIAMOND;
import static bbidder.Constants.HEART;
import static bbidder.Constants.SPADE;
import static bbidder.Constants.STR_CLUB;
import static bbidder.Constants.STR_DIAMOND;
import static bbidder.Constants.STR_HEART;
import static bbidder.Constants.STR_SPADE;

import java.util.List;


public class SimpleContext implements Context {

    public final BidContext bc;
    
    public final LikelyHands likelyHands;
    
    public final Bid lastBidSuit;

    public SimpleContext(Bid lastBidSuit, LikelyHands likelyHands, BidContext bc) {
        super();
        this.lastBidSuit = lastBidSuit;
        this.likelyHands = likelyHands;
        this.bc = bc;
    }

    public SimpleContext() {
        super();
        this.lastBidSuit = null;
        this.likelyHands = new LikelyHands();
        this.bc = new BidContext(new BidList(List.of()), new BidPatternList(List.of(), false), false);
    }

    @Override
    public int lookupSuit(String s) {
        switch (s.toUpperCase()) {
        case STR_CLUB:
            return CLUB;
        case STR_DIAMOND:
            return DIAMOND;
        case STR_HEART:
            return HEART;
        case STR_SPADE:
            return SPADE;
        default:
            Integer s2 = bc.getSuit(s);
            if (s2 == null) {
                throw new IllegalArgumentException("unknown suit: " + s);
            }
            return s2;
        }
    }

    @Override
    public short lookupSuitSet(String s) {
        switch (s.toUpperCase()) {
        case "UNBID": {
            short suits = 0 ;
            for(int suit = 0; suit < 4; suit++) {
                if (likelyHands.lho.avgLenInSuit(suit) < 4 && likelyHands.rho.avgLenInSuit(suit) < 4 &&
                        likelyHands.partner.avgLenInSuit(suit) < 4 && likelyHands.me.avgLenInSuit(suit) < 4) {
                    suits |= (1 << suit);
                }
            }
            return suits;
        }
        case "SAME_LEVEL": {
            if (lastBidSuit == null) {
                return 0xf;
            }
            short suits = 0 ;
            for(int suit = lastBidSuit.strain + 1; suit < 4; suit++) {
                suits |= (1 << suit);
            }
            return suits;
        }
        case "MINORS":
            return (1 << CLUB) | (1 << DIAMOND);
        case "MAJORS":
            return (1 << HEART) | (1 << SPADE);
        case "REDS":
            return (1 << HEART) | (1 << DIAMOND);
        case "BLACKS":
            return (1 << SPADE) | (1 << CLUB);
        case "ROUND":
            return (1 << HEART) | (1 << CLUB);
        case "POINTED":
            return (1 << SPADE) | (1 << DIAMOND);
        case "ALL":
            return 0xf;
        case "NONE":
            return 0;
        case STR_CLUB:
            return (1 << CLUB);
        case STR_DIAMOND:
            return (1 << DIAMOND);
        case STR_HEART:
            return (1 << HEART);
        case STR_SPADE:
            return (1 << SPADE);
        }
        if (s.startsWith("~")) {
            return (short)(~lookupSuitSet(s.substring(1)) & 0xf);
        }
        throw new IllegalArgumentException("unknown suit set: " + s);
    }

    @Override
    public int resolvePoints(String s) {
        if (s.matches("\\d+")) {
            return Integer.parseInt(s);
        }
        throw new IllegalArgumentException("unknown points: " + s);
    }

    @Override
    public int resolveLength(String s) {
        if (s.matches("\\d+")) {
            return Integer.parseInt(s);
        }
        throw new IllegalArgumentException("unknown length: " + s);
    }
}
