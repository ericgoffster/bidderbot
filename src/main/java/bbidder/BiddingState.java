package bbidder;

import java.util.Arrays;
import java.util.List;

import bbidder.inferences.AndBoundInference;
import bbidder.inferences.ConstBoundInference;

/**
 * Represents the entre state of an entire bidding auction
 * for all players.
 * 
 * @author goffster
 *
 */
public class BiddingState {
    public final BiddingSystem[] systems;
    public final BidList bidding;
    public final int turn;
    public final IBoundInference[] players;

    public BiddingState(BiddingSystem system) {
        this(new BiddingSystem[] { system, system }, 0);
    }

    public BiddingState(BiddingSystem[] systems, int turn) {
        this.systems = systems;
        this.turn = turn;
        this.bidding = new BidList(List.of());
        this.players = new IBoundInference[] { ConstBoundInference.T, ConstBoundInference.T, ConstBoundInference.T, ConstBoundInference.T };
    }

    public BiddingState(BiddingSystem[] systems, BidList bidding, int turn, IBoundInference[] players) {
        super();
        this.systems = systems;
        this.bidding = bidding;
        this.turn = turn;
        this.players = players;
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new Bidding State with the given bid made.
     */
    public BiddingState withBid(Bid bid) {
        BidList newBidList = bidding.withBidAdded(bid);
        IBoundInference[] newPlayers = Arrays.copyOf(players, players.length);
        LikelyHands likelyHands = new LikelyHands(newPlayers[(turn + 3) % 4], newPlayers[(turn + 2) % 4], newPlayers[(turn + 1) % 4],
                newPlayers[turn]);
        newPlayers[turn] = AndBoundInference.create(systems[turn % 2].getInference(newBidList, likelyHands), newPlayers[turn]);
        return new BiddingState(systems, newBidList, (turn + 1) % 4, newPlayers);
    }

    /**
     * @param hand
     *            The hand
     * @return A bid for the given hand.
     */
    public Bid getBid(Hand hand) {
        // Retrieve likeihood of all hands for all players
        LikelyHands likelyHands = new LikelyHands(players[(turn + 3) % 4], players[(turn + 2) % 4], players[(turn + 1) % 4], players[turn]);

        // Get the bid from the system.
        return systems[turn % 2].getBid(bidding, likelyHands, hand);
    }
}
