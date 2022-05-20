package bbidder;

import java.util.Objects;

import bbidder.utils.BitUtil;

/**
 * A Stoppers represents the set of all suits we have a stopper in.
 * This could have been treated as an ordinal.
 * @author goffster
 *
 */
public final class Stoppers {
    public static Stoppers EMPTY = new Stoppers((byte) 0);
    private final short patt;

    /**
     * Constructs a Stoppers from a bit pattern of suits.
     * @param patt a bit pattern of suits
     */
    private Stoppers(short patt) {
        super();
        this.patt = patt;
    }

    /**
     * @param suit The suit to test
     * @return Return true if we have a stopper in the given suit.
     */
    public boolean stopperIn(int suit) {
        return ((1 << suit) & patt) != 0;
    }

    /**
     * 
     * @param suit The suit to add.
     * @return A new stoppers with a stopper in the given suit.
     */
    public Stoppers withStopperIn(int suit) {
        return values[patt | (1 << suit)];
    }
    
    /**
     * 
     * @param other The other stoppers to "|" with
     * @return A new stoppers with stoppers in "this" | stoppers in the "other"
     */
    public Stoppers or(Stoppers other) {
        return values[patt | other.patt];
    }

    @Override
    public int hashCode() {
        return Objects.hash(patt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stoppers other = (Stoppers) obj;
        return patt == other.patt;
    }

    @Override
    public String toString() {
        return BitUtil.stream(patt)
                .mapToObj(i -> Strain.getName(i))
                .reduce(new StringBuilder(), (sb, i) -> sb.append(i), (a, b) -> a.append(b))
                .toString();
    }

    public int ordinal() {
        return patt;
    }

    public static Stoppers[] values = new Stoppers[16];
    static {
        for (int i = 0; i < 16; i++) {
            values[i] = new Stoppers((short) i);
        }
    }

    public static Stoppers[] values() {
        return values;
    }
}
