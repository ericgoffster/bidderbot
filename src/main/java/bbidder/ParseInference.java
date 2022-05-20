package bbidder;

import java.util.ArrayList;
import java.util.List;

import bbidder.parsers.BidInferenceParser;

public class ParseInference {
    public static void main(String[] args) {
        BidInference unresolved = BidInferenceParser.parseBidInference("", "1m (X) 1y:down => gf+, 5+ y, longest_or_equal y among unbid",
                BidPatternList.EMPTY);
        List<BidInference> resolved = new ArrayList<>();
        unresolved.resolveSuits().forEach(resolved::add);
        System.out.println(unresolved);
        for (BidInference bi : resolved) {
            System.out.println(bi);
        }
    }
}
