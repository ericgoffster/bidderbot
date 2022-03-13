package bbidder;

import java.util.List;
import java.util.Random;

import bbidder.BiddingSystem.BidSource;
import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;

/**
 * Represents the entre state of an entire bidding auction
 * for all players.
 * 
 * @author goffster
 *
 */
public class BiddingState {
    public final BiddingSystem we;
    public final BiddingSystem they;
    public final BidList bidding;
    public final Player lho;
    public final Player partner;
    public final Player rho;
    public final Player me;
    public final Random r;

    public BiddingState(Random r, BiddingSystem system) {
        this(r, system, system);
    }

    public BiddingState(Random r, BiddingSystem we, BiddingSystem they) {
        this.r = r;
        this.we = we;
        this.they = they;
        this.bidding = new BidList(List.of());
        this.lho = new Player();
        this.partner = new Player();
        this.me = new Player();
        this.rho = new Player();
    }

    public BiddingState(Random r, BiddingSystem we, BiddingSystem they, BidList bidding, Player lho, Player partner, Player rho, Player me) {
        super();
        this.r = r;
        this.we = we;
        this.they = they;
        this.bidding = bidding;
        this.lho = lho;
        this.partner = partner;
        this.rho = rho;
        this.me = me;
    }

    public static class Player {
        final IBoundInference inf;
        final InfSummary likelyHand;

        public Player(IBoundInference inf, InfSummary summ) {
            super();
            this.inf = inf;
            this.likelyHand = summ;
        }

        public Player() {
            this.inf = ConstBoundInference.T;
            this.likelyHand = InfSummary.ALL;
        }
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new Bidding State with the given bid made.
     */
    public BiddingState withBid(Bid bid) {
        BidList newBidList = bidding.withBidAdded(bid);
        IBoundInference newInf = AndBoundInf.create(we.getInference(newBidList, getLikelyHands()), me.inf);
        InfSummary summ = newInf.getSummary();
        Player newMe = new Player(newInf, summ);
        return new BiddingState(r, they, we, newBidList, partner, rho, newMe, lho);
    }

    public LikelyHands getLikelyHands() {
        return new LikelyHands(lho.likelyHand, partner.likelyHand, rho.likelyHand, me.likelyHand);
    }

    /**
     * @param hand
     *            The hand
     * @return A bid for the given hand.
     */
    public BidSource getBid(Hand hand) {
        // Get the bid from the system.
        return we.getBid(bidding, getLikelyHands(), hand);
    }
}
