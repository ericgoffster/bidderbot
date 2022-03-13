package bbidder;

public class LikelyHands {
    public final IHandList lho;
    public final IHandList partner;
    public final IHandList rho;
    public final IHandList me;

    public LikelyHands() {
        this(new AllPossibleHands(), new AllPossibleHands(), new AllPossibleHands(), new AllPossibleHands());
    }

    public LikelyHands(IHandList lho, IHandList parter, IHandList rho, IHandList me) {
        super();
        this.lho = lho;
        this.partner = parter;
        this.rho = rho;
        this.me = me;
    }
}
