package bbidder.inferences;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RangeOf {
    public static Pattern PATT_MIN_TO_MAX = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(\\d+)\\s*(.*)");
    public static Pattern PATT_MAX = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(.*)");
    public static Pattern PATT_MIN = Pattern.compile("\\s*(\\d+)\\s*\\+\\s*(.*)");
    public static Pattern PATT_EXACT = Pattern.compile("\\s*(\\d+)\\s*(.*)");

    public final Integer min;
    public final Integer max;
    public final String of;

    public RangeOf(Integer min, Integer max, String of) {
        super();
        this.min = min;
        this.max = max;
        this.of = of;
    }

    public static RangeOf valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m;
        m = PATT_MIN_TO_MAX.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), m.group(3).trim());
        }

        m = PATT_MAX.matcher(str);
        if (m.matches()) {
            return new RangeOf(null, Integer.parseInt(m.group(1)), m.group(2).trim());
        }
        m = PATT_MIN.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), null, m.group(2).trim());
        }
        m = PATT_EXACT.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(1)), m.group(2).trim());
        }
        return null;
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
