package bbidder.generalities;

import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;

public final class TrueGenerality extends Generality {
    public static TrueGenerality T = new TrueGenerality();

    private TrueGenerality() {
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return Stream.of(new Context(suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == T;
    }

    public static TrueGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        if (str.trim().equalsIgnoreCase("true")) {
            return new TrueGenerality();
        }
        return null;
    }
}
