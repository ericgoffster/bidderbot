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
    
    public Players rotate() {
        return new Players(partner, rho, me, lho);
    }
    
    public Players withNewMe(Player me) {
        return new Players(lho, partner, rho, me);
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
    
    public Optional<Integer> ourCombinedMinLength(int s) {
        Optional<Integer> meLenLonger = me.infSummary.minLenInSuit(s);
        Optional<Integer> partnerLenLonger = partner.infSummary.minLenInSuit(s);
        return meLenLonger.flatMap(meLen -> partnerLenLonger.map(partnerLen -> meLen + partnerLen));
    }
    
    public boolean iBidSuit(int s) {
        if (ourCombinedMinLength(s).filter(ourLen -> ourLen >= 8).isPresent()) {
            return false;
        }
        Optional<Short> bidSuits = me.infSummary.getBidSuits();
        if (bidSuits.isEmpty()) {
            return false;
        }
        if ((bidSuits.get() & (1 << s)) == 0) {
            return false;
        }
        return true;
    }
}
