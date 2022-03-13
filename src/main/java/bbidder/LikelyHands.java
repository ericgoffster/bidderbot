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

    public LikelyHands(IBoundInference lho, IBoundInference parter, IBoundInference rho, IBoundInference me) {
        super();
        this.lho = HandGenerator.generateHands(lho, 1000);
        this.partner = HandGenerator.generateHands(parter, 1000);
        this.rho = HandGenerator.generateHands(rho, 1000);
        this.me = HandGenerator.generateHands(me, 1000);
    }
}
