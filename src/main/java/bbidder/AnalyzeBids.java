package bbidder;

import bbidder.parsers.AuctionParser;
import bbidder.parsers.BiddingSystemParser;

public class AnalyzeBids {
    public static void main(String[] args) {
        BiddingSystem bs = BiddingSystemParser.load("classpath:bbo21/index.bidding", ex -> {
            throw new RuntimeException(ex);
        });
        Auction auction = AuctionParser.parseAuction("1C (X)");

        System.out.println(auction);

        BiddingState state = new BiddingState(bs);
        for (Bid bid : auction.getBids()) {
            state = state.withBid(bid);
        }

        for (var pb : state.getPossibleBids()) {
            System.out.println(pb.inf.where);
            System.out.println(pb.inf);
        }
    }
}
