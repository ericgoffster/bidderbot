package bbidder.generalities;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.PointRange;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class TotalPointsEstablished extends Generality {
    public final PointRange rng;

    public TotalPointsEstablished(PointRange combined) {
        super();
        this.rng = combined;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        OptionalInt ourCombinedMinLength = players.ourCombinedMinTpts();
        if (!ourCombinedMinLength.isPresent()) {
            return false;
        }
        return rng.contains(ourCombinedMinLength.getAsInt());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(rng);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TotalPointsEstablished other = (TotalPointsEstablished) obj;
        return Objects.equals(rng, other.rng);
    }

    @Override
    public String toString() {
        return rng.toString();
    }

}