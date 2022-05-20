package bbidder;

import java.util.List;

import bbidder.inferences.bound.AndBoundInf;
import bbidder.utils.DebugUtils;

/**
 * Represents the entire state of an entire bidding auction
 * for all players.
 * 
 * @author goffster
 *
 */
public final class BiddingState {
    public final BiddingSystem we;
    public final BiddingSystem they;
    public final Auction bidding;
    public final Players players;

    private BiddingState(BiddingSystem we, BiddingSystem they, Auction bidding, Players players) {
        super();
        this.we = we;
        this.they = they;
        this.bidding = bidding;
        this.players = players;
    }

    /**
     * Constructs an initial bidding state with everyone playing the given system
     * 
     * @param system
     *            The global system
     */
    public BiddingState(BiddingSystem system) {
        this(system, system);
    }

    /**
     * Constructs an initial bidding state with everyone playing their own system
     * 
     * @param we
     *            The bidding system for "we"
     * @param they
     *            The bidding system for "they"
     */
    public BiddingState(BiddingSystem we, BiddingSystem they) {
        this.we = we;
        this.they = they;
        this.bidding = Auction.create(List.of());
        this.players = new Players();
    }
    
    public BiddingState withNewInference(IBoundInference inference) {
        IBoundInference newInf = AndBoundInf.create(inference, players.me.inf);
        InfSummary newSummary = newInf.getSummary();
        return new BiddingState(we, they, bidding, players.withNewMe(new Player(newInf, newSummary)));
    }
    
    public BiddingState rotate(int n) {
        BiddingSystem newWe = n % 2 == 0 ? we : they;
        BiddingSystem newThey = n % 2 == 0 ? they : we;
        return new BiddingState(newWe, newThey, bidding, players.rotate(n));
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new Bidding State with the given bid made.
     */
    public BiddingState withBid(Bid bid) {
        Auction newBidList = bidding.withBidAdded(bid);
        IBoundInference inference = we.getInference(newBidList, players);
        IBoundInference newInf = AndBoundInf.create(inference, players.me.inf);
        InfSummary newSummary = newInf.getSummary();
        InfSummary oldSummary = players.me.infSummary;
        if (!oldSummary.isEmpty() && newSummary.isEmpty()) {
            DebugUtils.breakpointNoBid(bidding, bid, players);
        }
        return new BiddingState(we, they, newBidList, players.withNewMe(new Player(newInf, newSummary))).rotate(1);
    }

    /**
     * @param hand
     *            The hand
     * @return A bid for the given hand.
     */
    public BidSource getBid(Hand hand) {
        return we.getBid(bidding, players, hand);
    }
    
    public List<PossibleBid> getPossibleBids() {
        return we.getPossibleBids(bidding, players);
    }
}
