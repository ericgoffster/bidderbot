package bbidder;

import java.util.Optional;
import java.util.OptionalInt;

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
    
    public OptionalInt ourCombinedMinLength(int s) {
        OptionalInt meLenLonger = me.infSummary.minLenInSuit(s);
        if (!meLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        OptionalInt partnerLenLonger = partner.infSummary.minLenInSuit(s);
        if (!partnerLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        int meLen = meLenLonger.getAsInt();
        int partnerLen = partnerLenLonger.getAsInt();
        return OptionalInt.of(meLen + partnerLen);
    }
    
    public boolean iBidSuit(int s) {
        OptionalInt ourCombinedMinLength = ourCombinedMinLength(s);
        if (!ourCombinedMinLength.isPresent()) {
            return false;
        }
        if (ourCombinedMinLength.getAsInt() >= 8) {
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
