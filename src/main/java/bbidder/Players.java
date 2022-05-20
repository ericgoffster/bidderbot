package bbidder;

import java.util.Optional;

public final class Players {
    public final Player lho;
    public final Player partner;
    public final Player rho;
    public final Player me;

    public Players() {
        this(Player.ALL, Player.ALL, Player.ALL, Player.ALL);
    }

    public Players(Player lho, Player parter, Player rho, Player me) {
        super();
        this.lho = lho;
        this.partner = parter;
        this.rho = rho;
        this.me = me;
    }

    @Override
    public String toString() {
        return "lho=" + lho + ", partner=" + partner + ", rho=" + rho + ", me=" + me;
    }
    
    public boolean isBidSuit(Player me, Player partner, int s) {
        Optional<Integer> meLenLonger = me.infSummary.minLenInSuit(s);
        Optional<Integer> partnerLenLonger = partner.infSummary.minLenInSuit(s);
        if (!meLenLonger.isPresent() || !partnerLenLonger.isPresent()) {
            return false;
        }
        if (meLenLonger.get() + partnerLenLonger.get() >= 8) {
            return false;
        }
        if ((me.infSummary.getBidSuits() & (1 << s)) == 0) {
            return false;
        }
        return true;
    }
}
