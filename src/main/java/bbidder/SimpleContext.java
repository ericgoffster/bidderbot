package bbidder;

import java.util.function.Function;
import static bbidder.Constants.*;


public class SimpleContext implements Context {

    public final Function<String, Integer> lookupSuit;
    
    public final LikelyHands likelyHands;

    public SimpleContext(LikelyHands likelyHands, Function<String, Integer> lookupSuit) {
        super();
        this.likelyHands = likelyHands;
        this.lookupSuit = lookupSuit;
    }

    public SimpleContext() {
        super();
        this.likelyHands = new LikelyHands();
        this.lookupSuit = s -> {
            throw new IllegalArgumentException("unknown suit " + s);
        };
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
            Integer s2 = lookupSuit.apply(s);
            if (s2 == null) {
                throw new IllegalArgumentException("unknown suit: " + s);
            }
            return lookupSuit.apply(s);
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
        throw new IllegalArgumentException("unknown suit " + s);
    }

    @Override
    public int resolvePoints(String s) {
        if (s.matches("\\d+")) {
            return Integer.parseInt(s);
        }
        throw new IllegalArgumentException("unknown points " + s);
    }

    @Override
    public int resolveLength(String s) {
        if (s.matches("\\d+")) {
            return Integer.parseInt(s);
        }
        throw new IllegalArgumentException("unknown length " + s);
    }
}
