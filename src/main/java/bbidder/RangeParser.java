package bbidder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RangeParser {
    private static Pattern PATT_MIN_TO_MAX = Pattern.compile("(\\d+)\\s*\\-\\s*(\\d+)\\s*(.*)");
    private static Pattern PATT_MAX = Pattern.compile("(\\d+)\\s*\\-\\s*(.*)");
    private static Pattern PATT_MIN = Pattern.compile("(\\d+)\\s*\\+\\s*(.*)");
    private static Pattern PATT_EXACT = Pattern.compile("(\\d+)\\s*(.*)");
    private static Pattern PATT_MAX2 = Pattern.compile("max\\s*(.*)");

    public static RangeOf parseRange(String str) {
        if (str == null) {
            return null;
        }
        Matcher m;
        m = PATT_MIN_TO_MAX.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), m.group(3).trim(), false);
        }
    
        m = PATT_MAX2.matcher(str);
        if (m.matches()) {
            return new RangeOf(null, null, m.group(1).trim(), true);
        }
        m = PATT_MAX.matcher(str);
        if (m.matches()) {
            return new RangeOf(null, Integer.parseInt(m.group(1)), m.group(2).trim(), false);
        }
        m = PATT_MIN.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), null, m.group(2).trim(), false);
        }
        m = PATT_EXACT.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(1)), m.group(2).trim(), false);
        }
        return null;
    }
}
