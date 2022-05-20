package bbidder.parsers;

import bbidder.BidInference;
import bbidder.BidPattern;
import bbidder.BidPatternList;
import bbidder.utils.SplitUtil;

public class BidInferenceParser {

    /**
     * @param where
     *            Where is it located.
     * @param str
     *            The string to parse.
     * @param prefix
     *            The prefix
     * @return A BidInference parsed from the string
     */
    public static BidInference parseBidInference(String where, String str, BidPatternList prefix) {
        if (str == null) {
            return null;
        }
        String description;
        int pos = str.indexOf("[[");
        if (pos >= 0) {
            int pos2 = str.indexOf("]]", pos);
            if (pos >= 0 && pos2 >= 0) {
                description = str.substring(pos + 2, pos2);
                str = str.substring(0, pos) + str.substring(pos2 + 2);
            } else {
                description = "";
            }
        } else {
            description = "";
        }
        String[] parts = SplitUtil.split(str, "=>", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid bid inference '" + str + "'");
        }
        for (BidPattern pattern : BidPatternListParser.parse(parts[0]).getBids()) {
            prefix = prefix.withBidAdded(pattern);
        }
        return new BidInference(description, where, prefix, InferenceParser.parseInference(parts[1]));
    }

}
