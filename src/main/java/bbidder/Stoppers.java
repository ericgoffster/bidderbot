package bbidder;

import java.util.Objects;

import bbidder.utils.BitUtil;

public final class Stoppers {
    public static Stoppers EMPTY = new Stoppers((byte) 0);
    private final byte patt;

    public Stoppers(byte patt) {
        super();
        this.patt = patt;
    }

    public boolean stopperIn(int suit) {
        return ((1 << suit) & patt) != 0;
    }

    public Stoppers withStopperIn(int suit) {
        return new Stoppers((byte) (patt | (1 << suit)));
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
        StringBuilder sb = new StringBuilder();
        for (int i : BitUtil.iterate(patt)) {
            sb.append(Strain.getName(i));
        }
        return sb.toString();
    }

    public int ordinal() {
        return patt;
    }

    public static Stoppers[] values = new Stoppers[16];
    static {
        for (int i = 0; i < 16; i++) {
            values[i] = new Stoppers((byte) i);
        }
        values[0] = values[0];
    }

    public static Stoppers[] values() {
        return values;
    }
}
