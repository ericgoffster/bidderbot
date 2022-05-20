package bbidder.suitsets;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.Players;
import bbidder.Position;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class BidSuits extends SuitSet {
    private final Position position;
    public BidSuits(Position position) {
        super();
        this.position = position;
    }

    @Override
    public short evaluate(Players players) {
        OptionalInt partnerSuits = players.getPlayer(position.getOpposite()).infSummary.getBidSuits();
        OptionalInt meSuits = players.getPlayer(position).infSummary.getBidSuits();
        if (!partnerSuits.isPresent() || !meSuits.isPresent()) {
            return 0;
        }
        int ourSuits = partnerSuits.getAsInt() | meSuits.getAsInt();
        return (short) (0xf ^ ourSuits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidSuits other = (BidSuits) obj;
        return position == other.position;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        switch(position) {
        case ME:
        case PARTNER:
            return "ours";
        case LHO:
        case RHO:
            return "theirs";
        default:
            throw new IllegalStateException();
        }
    }
}