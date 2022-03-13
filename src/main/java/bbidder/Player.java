package bbidder;

import bbidder.inferences.bound.ConstBoundInference;

/**
 * Represents a immutable player in a bidding state.
 * @author goffster
 *
 */
public final class Player {
    public final IBoundInference inf;
    public final InfSummary infSummary;
    public static final Player ALL = new Player(ConstBoundInference.T, InfSummary.ALL);

    public Player(IBoundInference inf, InfSummary summ) {
        super();
        this.inf = inf;
        this.infSummary = summ;
    }

    @Override
    public String toString() {
        return infSummary.toString();
    }
}