package bbidder;

import java.util.List;

public class DebugUtils {
    public static boolean debugMode = false;
    public static int cnt;
    
    public static void breakpoint() {
        if (debugMode) {
            System.out.println("*************************");
        }
    }

    public static void breakpointGetPossibleBid(BidList bids, Players players, Bid match, BidInference i) {
        if (debugMode) {
            System.out.println("getPossible bids:");
            System.out.println("   bids:" + bids);
            System.out.println("   me:" + players.me.infSummary);
            System.out.println("   partner:" + players.partner.infSummary);
            System.out.println("   match:" + match);
            System.out.println("   inference:" + i);
        }
    }

    public static void breakpointGetPossibleBid(BidList bids, Players players) {
        if (debugMode) {
        }        
    }

    public static void breakpointGetPossibleBid(BidList bids, Players players, List<PossibleBid> l) {
        if (debugMode) {
            if (l.isEmpty()) {
                System.out.println("getPossible bids returned nothing:");
                System.out.println("   bids:" + bids);
                System.out.println("   me:" + players.me.infSummary);
                System.out.println("   partner:" + players.partner.infSummary);
            }
        }        
    }

    public static void breakpointNoBid(BidList bidding, Bid bid, Players players) {
        if (debugMode) {
            System.out.println(bid + " made no sense for "+ bidding);
            System.out.println("   me:" + players.me.infSummary);
            System.out.println("   partner:" + players.partner.infSummary);
        }
    }
}
