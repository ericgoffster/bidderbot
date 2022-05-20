package bbidder;

import java.util.Objects;

public final class RangeOf {
    public final Integer min;
    public final Integer max;
    public final String of;
    public final boolean maxPromised;

    public RangeOf(Integer min, Integer max, String of, boolean maxPromised) {
        super();
        this.min = min;
        this.max = max;
        this.of = of;
        this.maxPromised = maxPromised;
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, maxPromised, min, of);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RangeOf other = (RangeOf) obj;
        return Objects.equals(max, other.max) && maxPromised == other.maxPromised && Objects.equals(min, other.min) && Objects.equals(of, other.of);
    }

    @Override
    public String toString() {
        if (maxPromised) {
            return "max " + of;
        }
        if (min == null) {
            return max + "- " + of;
        }
        if (max == null) {
            return min + "+ " + of;
        }
        if (max.intValue() == min.intValue()) {
            return min + " " + of;
        }
        return min + "-" + max + " " + of;
    }
}
