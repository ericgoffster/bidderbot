package bbidder;

import java.util.function.Function;

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
        case "C":
            return 0;
        case "D":
            return 1;
        case "H":
            return 2;
        case "S":
            return 3;
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
            return 0x3;
        case "MAJORS":
            return 0x3 << 2;
        case "REDS":
            return (1 << 2) | (1 << 1);
        case "BLACKS":
            return (1 << 3) | (1 << 0);
        case "ROUND":
            return (1 << 2) | (1 << 0);
        case "POINTED":
            return (1 << 3) | (1 << 1);
        case "ALL":
            return 0xf;
        case "NONE":
            return 0;
        case "C":
            return (1 << 0);
        case "D":
            return (1 << 1);
        case "H":
            return (1 << 2);
        case "S":
            return (1 << 3);
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
