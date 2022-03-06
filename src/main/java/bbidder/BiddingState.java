package bbidder;

import java.util.Arrays;
import java.util.List;

import bbidder.inferences.AndBoundInference;
import bbidder.inferences.ConstBoundInference;

public class BiddingState {
    public final BiddingSystem[] systems;
    public final BidList bidding;
    public final int turn;
    public final IBoundInference[] players;
    public BiddingState(BiddingSystem system) {
        this(new BiddingSystem[] {system, system}, 0);
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
    
    public BiddingState addBid(Bid bid) {
        BidList newBidList = bidding.addBid(bid);
        IBoundInference[] newPlayers = Arrays.copyOf(players, players.length);
        newPlayers[turn] = AndBoundInference.create(systems[turn % 2].getInference(newBidList), newPlayers[turn]);
        return new BiddingState(systems, newBidList, (turn + 1) % 4, newPlayers);
    }
}
