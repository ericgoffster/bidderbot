package bbidder;

import java.util.Objects;

public final class RangeOf {
    public final Integer min;
    public final Integer max;
    public final String of;

    public RangeOf(Integer min, Integer max, String of) {
        super();
        this.min = min;
        this.max = max;
        this.of = of;
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, min, of);
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
        return Objects.equals(max, other.max) && Objects.equals(min, other.min) && Objects.equals(of, other.of);
    }

    @Override
    public String toString() {
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
