package bbidder;

import java.util.List;

public interface Generality {
    public List<BiddingContext> resolveSymbols(BiddingContext context);

    public boolean matches(Players players, BidList bidList);
}
