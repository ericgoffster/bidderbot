package bbidder;

import java.util.List;
import java.util.Random;

import bbidder.BiddingSystem.BidSource;
import bbidder.inferences.bound.AndBoundInf;

/**
 * Represents the entre state of an entire bidding auction
 * for all players.
 * 
 * @author goffster
 *
 */
public final class BiddingState {
    public final BiddingSystem we;
    public final BiddingSystem they;
    public final BidList bidding;
    public final Players players;
    
    public BiddingState(Random r, BiddingSystem system) {
        this(system, system);
    }

    public BiddingState(BiddingSystem we, BiddingSystem they) {
        this.we = we;
        this.they = they;
        this.bidding = BidList.create(List.of());
        this.players = new Players();
    }

    public BiddingState(BiddingSystem we, BiddingSystem they, BidList bidding, Players players) {
        super();
        this.we = we;
        this.they = they;
        this.bidding = bidding;
        this.players = players;
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new Bidding State with the given bid made.
     */
    public BiddingState withBid(Bid bid) {
        BidList newBidList = bidding.withBidAdded(bid);
        IBoundInference newInf = AndBoundInf.create(we.getInference(newBidList, players), players.me.inf);
        InfSummary summ = newInf.getSummary();
        Player newMe = new Player(newInf, summ);
        return new BiddingState(they, we, newBidList, new Players(players.partner, players.rho, newMe, players.lho));
    }

    /**
     * @param hand
     *            The hand
     * @return A bid for the given hand.
     */
    public BidSource getBid(Hand hand) {
        // Get the bid from the system.
        return we.getBid(bidding, players, hand);
    }
}
