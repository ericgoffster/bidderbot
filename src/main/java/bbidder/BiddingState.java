package bbidder;

public class BiddingState {
    public final BiddingSystem we;
    public final BiddingSystem they;
    public final BidList bidding;
    public final int turn;
    public final IBoundInference lho;
    public final IBoundInference partner;
    public final IBoundInference rho;
    public final IBoundInference me;
    public BiddingState(BiddingSystem we, BiddingSystem they, BidList bidding, int turn, IBoundInference lho, IBoundInference partner,
            IBoundInference rho, IBoundInference me) {
        super();
        this.we = we;
        this.they = they;
        this.bidding = bidding;
        this.turn = turn;
        this.lho = lho;
        this.partner = partner;
        this.rho = rho;
        this.me = me;
    }
}
