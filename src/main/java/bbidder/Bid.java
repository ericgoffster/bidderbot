package bbidder;

public enum Bid {
    _1C(0, 0),
    _1D(1, 0),
    _1H(2, 0),
    _1S(3, 0),
    _1N(4, 0),
    _2C(0, 1),
    _2D(1, 1),
    _2H(2, 1),
    _2S(3, 1),
    _2N(4, 1),
    _3C(0, 2),
    _3D(1, 2),
    _3H(2, 2),
    _3S(3, 2),
    _3N(4, 2),
    _4C(0, 3),
    _4D(1, 3),
    _4H(2, 3),
    _4S(3, 3),
    _4N(4, 3),
    _5C(0, 4),
    _5D(1, 4),
    _5H(2, 4),
    _5S(3, 4),
    _5N(4, 4),
    _6C(0, 5),
    _6D(1, 5),
    _6H(2, 5),
    _6S(3, 5),
    _6N(4, 5),
    _7C(0, 6),
    _7D(1, 6),
    _7H(2, 6),
    _7S(3, 6),
    _7N(4, 6),
    X(-1, 2),
    XX(-1, 4),
    P(-1, 1);
    ;

    public final int strain;
    public final int level;

    private Bid(int strain, int level) {
        this.strain = strain;
        this.level = level;
    }

    public boolean isSuitBid() {
        return strain >= 0;
    }

    @Override
    public String toString() {
        if (this == P) {
            return "P";
        }
        if (this == X) {
            return "X";
        }
        if (this == XX) {
            return "XX";
        }
        return String.valueOf(level + 1) + "CDHSN".charAt(strain);
    }

    static Integer getStrain(String c) {
        switch (c.toLowerCase()) {
        case "c":
            return 0;
        case "d":
            return 1;
        case "h":
            return 2;
        case "s":
            return 3;
        case "n":
            return 4;
        default:
            return null;
        }
    }

    public Bid raise() {
        return valueOf(level + 1, strain);
    }

    public static Bid fromStr(String str) {
        if (str.equalsIgnoreCase("P")) {
            return P;
        }
        if (str.equalsIgnoreCase("X")) {
            return X;
        }
        if (str.equalsIgnoreCase("XX")) {
            return XX;
        }
        Integer strain = getStrain(str.substring(1));
        if (strain == null) {
            throw new IllegalArgumentException("Invalid strain: " + str);
        }
        return valueOf(Integer.parseInt(str.substring(0, 1)) - 1, strain.intValue());
    }

    public static Bid valueOf(int level, int strain) {
        switch (strain) {
        case 0:
            switch (level) {
            case 0:
                return _1C;
            case 1:
                return _2C;
            case 2:
                return _3C;
            case 3:
                return _4C;
            case 4:
                return _5C;
            case 5:
                return _6C;
            case 6:
                return _7C;
            default:
                throw new IllegalArgumentException("invalid level " + level);
            }
        case 1:
            switch (level) {
            case 0:
                return _1D;
            case 1:
                return _2D;
            case 2:
                return _3D;
            case 3:
                return _4D;
            case 4:
                return _5D;
            case 5:
                return _6D;
            case 6:
                return _7D;
            default:
                throw new IllegalArgumentException("invalid level " + level);
            }
        case 2:
            switch (level) {
            case 0:
                return _1H;
            case 1:
                return _2H;
            case 2:
                return _3H;
            case 3:
                return _4H;
            case 4:
                return _5H;
            case 5:
                return _6H;
            case 6:
                return _7H;
            default:
                throw new IllegalArgumentException("invalid level " + level);
            }
        case 3:
            switch (level) {
            case 0:
                return _1S;
            case 1:
                return _2S;
            case 2:
                return _3S;
            case 3:
                return _4S;
            case 4:
                return _5S;
            case 5:
                return _6S;
            case 6:
                return _7S;
            default:
                throw new IllegalArgumentException("invalid level " + level);
            }
        case 4:
            switch (level) {
            case 0:
                return _1N;
            case 1:
                return _2N;
            case 2:
                return _3N;
            case 3:
                return _4N;
            case 4:
                return _5N;
            case 5:
                return _6N;
            case 6:
                return _7N;
            default:
                throw new IllegalArgumentException("invalid level " + level);
            }
        default:
            throw new IllegalArgumentException("invalid strain " + strain);
        }
    }

    public boolean isMajor() {
        return isSuitBid() && strain >= 2 && strain < 4;
    }

    public boolean isMinor() {
        return isSuitBid() && strain < 2;
    }
}
