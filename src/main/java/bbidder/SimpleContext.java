package bbidder;

import java.util.function.Function;

public class SimpleContext implements Context {
    
    public final Function<String, Integer> lookupSuit;
    
    
    public SimpleContext(Function<String, Integer> lookupSuit) {
        super();
        this.lookupSuit = lookupSuit;
    }

    public SimpleContext() {
        super();
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
            return lookupSuit.apply(s);
        }
    }

    @Override
    public int lookupSuitSet(String s) {
        switch (s.toUpperCase()) {
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
            return ~lookupSuitSet(s.substring(1)) & 0xf;
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
