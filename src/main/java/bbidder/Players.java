package bbidder;

import java.util.OptionalInt;

public final class Players {
    public final Player lho;
    public final Player partner;
    public final Player rho;
    public final Player me;
    
    public Player getPlayer(Position position) {
        switch(position) {
        case ME: return me;
        case PARTNER: return partner;
        case LHO: return lho;
        case RHO: return rho;
        default: throw new IllegalArgumentException();
        }
        
    }

    public Players() {
        this(Player.ALL, Player.ALL, Player.ALL, Player.ALL);
    }

    public Players rotate() {
        return rotate(1);
    }

    public Players rotate(int n) {
        Players p = this;
        while (n > 0) {
            p = new Players(p.partner, p.rho, p.me, p.lho);
            n--;
        }
        while (n < 0) {
            p = new Players(p.me, p.lho, p.partner, p.rho);
            n++;
        }
        return p;
    }

    public Players flip() {
        return rotate(2);
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

    public OptionalInt ourCombinedMinTpts() {
        OptionalInt meLenLonger = me.infSummary.tpts.lowest();
        if (!meLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        OptionalInt partnerLenLonger = partner.infSummary.tpts.lowest();
        if (!partnerLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        int meLen = meLenLonger.getAsInt();
        int partnerLen = partnerLenLonger.getAsInt();
        return OptionalInt.of(meLen + partnerLen);
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
    
    public boolean bidSuit(Position position, int s) {
        Player i = getPlayer(position);
        OptionalInt bidSuits = i.infSummary.getBidSuits();
        if (bidSuits.isEmpty()) {
            return false;
        }
        if ((bidSuits.getAsInt() & (1 << s)) == 0) {
            return false;
        }
        return true;
    }

    public boolean iBidSuit(int s) {
        return bidSuit(Position.ME,s);
    }
}
