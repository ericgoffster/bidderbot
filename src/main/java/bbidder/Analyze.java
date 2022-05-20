package bbidder;

import bbidder.utils.DebugUtils;

public class Analyze {
    public static void showSummary(String label, InfSummary summary) {
        ShapeStats[] stats = summary.shape.getStats();
        System.err.println(label);
        System.err.println("   Spades: " + stats[3]);
        System.err.println("   Hearts: " + stats[2]);
        System.err.println("   Diamonds: " + stats[1]);
        System.err.println("   Clubs: " + stats[0]);
        System.err.println("   total points: " + summary.tpts);
        System.err.println("   stoppers: " + summary.stoppers);
        System.err.println("   partial: " + summary.partialStoppers);
    }

    public static void main(String[] args) {
        BiddingSystem bs = BiddingSystemParser.load("classpath:bbo21/index.bidding", ex -> {
            throw new RuntimeException(ex);
        });
        Auction auction = Auction.valueOf(args[0]);

        System.out.println(auction);

        BiddingState state = new BiddingState(bs);
        for (Bid bid : auction.getBids()) {
            DebugUtils.breakpoint();
            state = state.withBid(bid);
        }
        
        state = state.rotate(-1);

        int i = 1;
        while(i < args.length) {
            String arg = args[i++];
            if (arg.equals("--whatif")) {
                Inference inference = InferenceParser.parseInference(args[i++]);
                IBoundInference newInf = inference.bind(state.players);
                state = state.withNewInference(newInf);
            }
        }
        
        System.out.println(state.players.me.inf);
        
        showSummary("me", state.players.me.infSummary);
        showSummary("partner", state.players.partner.infSummary);
        showSummary("lho", state.players.lho.infSummary);
        showSummary("rho", state.players.rho.infSummary);       
    }
}
