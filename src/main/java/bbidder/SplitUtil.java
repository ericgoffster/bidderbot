package bbidder;

/**
 * Various split utils for parsing.
 * 
 * @author goffster
 *
 */
public class SplitUtil {
    /**
     * Trims all strings in the array
     * 
     * @param l
     *            List of strings
     * @return Array of trimmed strings (null if l was null)
     */
    public static String[] trim(String[] l) {
        if (l == null) {
            return l;
        }
        String[] l2 = new String[l.length];
        for (int i = 0; i < l.length; i++) {
            l2[i] = l[i].trim();
        }
        return l2;
    }

    /**
     * Split the string by delimiter. The string is trimmed
     * before splitting, and if is is blank, a 0 length array is returned.
     * 
     * @param str
     *            The string to split.
     * @param delim
     *            The delimiter
     * @return A trimmed list of strings
     */
    public static String[] split(String str, String delim) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.equals("")) {
            return new String[0];
        }
        return trim(str.split(delim));
    }

    /**
     * Split the string by delimiter. The string is trimmed
     * before splitting, and if is is blank, a 0 length array is returned.
     * 
     * @param str
     *            The string to split.
     * @param delim
     *            The delimiter
     * @param n
     *            The max number to split on
     * @return A trimmed list of strings
     */
    public static String[] split(String str, String delim, int n) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.equals("")) {
            return new String[0];
        }
        return trim(str.split(delim, n));
    }
}
