package bbidder.generalities;

import java.util.Objects;

import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.TaggedAuction;
import bbidder.utils.MyStream;

public final class MadeBid extends Generality {
    public static final String NAME = "made_bid";
    private final String tag;
    private final int position;

    public MadeBid(int position, String tag) {
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
        for (int i = bids.size() - position; i >= 0; i -= 4) {
            if (bids.get(i).tags.contains(tag)) {
                return true;
            }
        }
        return false;
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

    public String getPosName() {
        switch (position) {
        case 4:
            return "i";
        case 1:
            return "rho";
        case 2:
            return "partner";
        case 3:
            return "lho";
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return getPosName() + " " + NAME + " " + tag;
    }

}
