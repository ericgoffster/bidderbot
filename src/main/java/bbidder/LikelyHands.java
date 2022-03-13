package bbidder;

public class LikelyHands {
    public final InfSummary lho;
    public final InfSummary partner;
    public final InfSummary rho;
    public final InfSummary me;

    public LikelyHands() {
        this(InfSummary.ALL, InfSummary.ALL, InfSummary.ALL, InfSummary.ALL);
    }

    public LikelyHands(InfSummary lho, InfSummary parter, InfSummary rho, InfSummary me) {
        super();
        this.lho = lho;
        this.partner = parter;
        this.rho = rho;
        this.me = me;
    }
}
