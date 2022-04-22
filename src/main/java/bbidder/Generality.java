package bbidder;

import java.util.List;

public interface Generality {
    public List<BidPatternContext> resolveSymbols(BidPatternContext context);

    public boolean matches(Players players, BidList bidList);
}
