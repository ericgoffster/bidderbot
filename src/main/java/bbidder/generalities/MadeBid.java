package bbidder.generalities;

import java.util.Objects;

import bbidder.Generality;
import bbidder.Players;
import bbidder.Position;
import bbidder.SuitTable;
import bbidder.TaggedAuction;
import bbidder.utils.MyStream;

public final class MadeBid extends Generality {
    public static final String NAME = "made_bid";
    private final String tag;
    private final Position position;

    public MadeBid(Position position, String tag) {
        super();
        this.position = position;
        this.tag = tag;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public boolean test(Players players, TaggedAuction bidList) {
        var bids = bidList.getBids();
        for (int i = bids.size() + getStart(); i >= 0; i -= 4) {
            if (bids.get(i).tags.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    private int getStart() {
        switch(position) {
        case ME: return -4;
        case LHO: return -3;
        case PARTNER: return -2;
        case RHO: return -1;
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, position);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MadeBid other = (MadeBid) obj;
        return Objects.equals(tag, other.tag) && Objects.equals(position, other.position);
    }

    @Override
    public String toString() {
        return position + " " + NAME + " " + tag;
    }

}
