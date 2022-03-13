package bbidder;

import bbidder.inferences.ConstBoundInference;

public class LikelyHands {
    public final IHandList lho;
    public final IHandList partner;
    public final IHandList rho;
    public final IHandList me;

    public LikelyHands() {
        this(ConstBoundInference.T, ConstBoundInference.T, ConstBoundInference.T, ConstBoundInference.T);
    }

    public LikelyHands(IBoundInference lho, IBoundInference parter, IBoundInference rho, IBoundInference me) {
        super();
        this.lho = HandGenerator.generateHands(lho, 1000);
        this.partner = HandGenerator.generateHands(parter, 1000);
        this.rho = HandGenerator.generateHands(rho, 1000);
        this.me = HandGenerator.generateHands(me, 1000);
    }
}
