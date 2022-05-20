package bbidder;

import java.util.Objects;

public final class RangeOf {
    public final Integer min;
    public final Integer max;
    public final boolean maxPromised;

    public RangeOf(Integer min, Integer max, boolean maxPromised) {
        super();
        this.min = min;
        this.max = max;
        this.maxPromised = maxPromised;
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, maxPromised, min);
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
        return Objects.equals(max, other.max) && maxPromised == other.maxPromised && Objects.equals(min, other.min);
    }

    @Override
    public String toString() {
        if (maxPromised) {
            return "max";
        }
        if (min == null) {
            return max + "-";
        }
        if (max == null) {
            return min + "+";
        }
        if (max.intValue() == min.intValue()) {
            return min + "";
        }
        return min + "-" + max;
    }
}
