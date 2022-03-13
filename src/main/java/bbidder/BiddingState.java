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
    public final Player[] players;

    public BiddingState(BiddingSystem system) {
        this(new BiddingSystem[] { system, system }, 0);
    }

    public BiddingState(BiddingSystem[] systems, int turn) {
        this.systems = systems;
        this.turn = turn;
        this.bidding = new BidList(List.of());
        this.players = new Player[] { new Player(), new Player(), new Player(), new Player() };
    }

    public BiddingState(BiddingSystem[] systems, BidList bidding, int turn, Player[] players) {
        super();
        this.systems = systems;
        this.bidding = bidding;
        this.turn = turn;
        this.players = players;
    }
    
    public static class Player {
        final IBoundInference inf;
        final IHandList likelyHand;
        public Player(IBoundInference inf, IHandList likelyHand) {
            super();
            this.inf = inf;
            this.likelyHand = likelyHand;
        }
        
        public Player() {
            this.inf = ConstBoundInference.T;
            this.likelyHand = new AllPossibleHands();
        }
    }
    
    public static IHandList getHandList(IBoundInference inf) {
        return HandGenerator.generateHands(inf, 1000);
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new Bidding State with the given bid made.
     */
    public BiddingState withBid(Bid bid) {
        BidList newBidList = bidding.withBidAdded(bid);
        LikelyHands likelyHands = getLikelyHands();
        IBoundInference newInf = AndBoundInference.create(systems[turn % 2].getInference(newBidList, likelyHands), players[turn].inf);
        Player[] newPlayers = Arrays.copyOf(players, players.length);
        IHandList newHands = HandGenerator.generateHands(newInf, 1000);
        newPlayers[turn] = new Player(newInf, newHands);
        return new BiddingState(systems, newBidList, (turn + 1) % 4, newPlayers);
    }

    private LikelyHands getLikelyHands() {
        return new LikelyHands(players[(turn + 3) % 4].likelyHand, players[(turn + 2) % 4].likelyHand, players[(turn + 1) % 4].likelyHand,
                players[turn].likelyHand);
    }

    /**
     * @param hand
     *            The hand
     * @return A bid for the given hand.
     */
    public Bid getBid(Hand hand) {
        // Get the bid from the system.
        LikelyHands likelyHands = getLikelyHands();
        return systems[turn % 2].getBid(bidding, likelyHands, hand);
    }
}
