package bbidder;

import bbidder.parsers.AuctionParser;
import bbidder.parsers.BiddingSystemParser;
import bbidder.parsers.InferenceParser;

public class Analyze {
    public static void main(String[] args) {
        BiddingSystem bs = BiddingSystemParser.load("classpath:bbo21/index.bidding", ex -> {
            throw new RuntimeException(ex);
        });
        Auction auction = AuctionParser.parseAuction(args[0]);

        System.out.println(auction);

        BiddingState state = new BiddingState(bs);
        for (Bid bid : auction.getBids()) {
            state = state.withBid(bid);
        }

        state = state.rotate(-1);

        int i = 1;
        while (i < args.length) {
            String arg = args[i++];
            if (arg.equals("--whatif")) {
                Inference inference = InferenceParser.parseInference(args[i++]);
                IBoundInference newInf = inference.bind(state.players);
                state = state.withNewInference(newInf);
            }
        }

        System.out.println(state.players.getPlayer(Position.ME).inf);

        state.players.getPlayer(Position.ME).infSummary.showSummary("me");
        state.players.getPlayer(Position.PARTNER).infSummary.showSummary("partner");
        state.players.getPlayer(Position.LHO).infSummary.showSummary("lho");
        state.players.getPlayer(Position.RHO).infSummary.showSummary("rho");
    }
}
