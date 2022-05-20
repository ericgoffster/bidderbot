package bbidder.generalities;

import java.util.Objects;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class ConstGenerality extends Generality {
    public static final String NAME = "true";
    public static ConstGenerality T = new ConstGenerality(true);
    public static ConstGenerality F = new ConstGenerality(false);
    public final boolean value;

    private ConstGenerality(boolean value) {
        this.value = value;
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
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(value);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
