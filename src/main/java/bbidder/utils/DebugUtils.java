package bbidder.utils;

import java.util.List;

import bbidder.Bid;
import bbidder.BidInference;
import bbidder.Players;
import bbidder.PossibleBid;
import bbidder.TaggedAuction;
import bbidder.TaggedBid;

public final class DebugUtils {
    public static boolean debugMode = false;
    public static int cnt;

    public static void breakpoint() {
        if (debugMode) {
            System.out.println("*************************");
        }
    }

    public static void breakpointGetPossibleBid(TaggedAuction bidding, Players players, TaggedBid match, BidInference i) {
        if (debugMode) {
            System.out.println("getPossible bids:");
            System.out.println("   bids:" + bidding);
            System.out.println("   me:" + players.me.infSummary);
            System.out.println("   partner:" + players.partner.infSummary);
            System.out.println("   match:" + match);
            System.out.println("   inference:" + i);
        }
    }

    public static void breakpointGetPossibleBid(TaggedAuction bidding, Players players) {
        if (debugMode) {
        }
    }

    public static void breakpointGetPossibleBid(TaggedAuction bidding, Players players, List<PossibleBid> l) {
        if (debugMode) {
            if (l.isEmpty()) {
                System.out.println("getPossible bids returned nothing:");
                System.out.println("   bids:" + bidding);
                System.out.println("   me:" + players.me.infSummary);
                System.out.println("   partner:" + players.partner.infSummary);
            }
        }
    }

    public static void breakpointNoBid(TaggedAuction bidding, Bid bid, Players players) {
        if (debugMode) {
            System.out.println(bid + " made no sense for " + bidding);
            System.out.println("   me:" + players.me.infSummary);
            System.out.println("   partner:" + players.partner.infSummary);
        }
    }
}
