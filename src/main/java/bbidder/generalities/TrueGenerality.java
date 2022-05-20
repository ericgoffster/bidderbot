package bbidder.generalities;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class TrueGenerality extends Generality {
    public static final String NAME = "true";
    public static TrueGenerality T = new TrueGenerality();

    private TrueGenerality() {
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        return true;
    }

    @Override
    public String toString() {
        return NAME;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == T;
    }
}
