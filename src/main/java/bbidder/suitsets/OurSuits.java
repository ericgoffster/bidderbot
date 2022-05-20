package bbidder.suitsets;

import java.util.OptionalInt;

import bbidder.Players;
import bbidder.Position;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class OurSuits extends SuitSet {
    public OurSuits() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        Position position = Position.ME;
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
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return "ours";
    }
}