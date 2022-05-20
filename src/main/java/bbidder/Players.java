package bbidder;

import java.util.OptionalInt;

public final class Players {
    private final Player lho;
    private final Player partner;
    public final Player rho;
    private final Player me;
    
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

    public OptionalInt combinedMinTpts(Position position) {
        OptionalInt meLenLonger = getPlayer(position).infSummary.tpts.lowest();
        if (!meLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        OptionalInt partnerLenLonger = getPlayer(position.getOpposite()).infSummary.tpts.lowest();
        if (!partnerLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        int meLen = meLenLonger.getAsInt();
        int partnerLen = partnerLenLonger.getAsInt();
        return OptionalInt.of(meLen + partnerLen);
    }

    public OptionalInt combinedMinLength(Position position, int s) {
        OptionalInt meLenLonger = getPlayer(position).infSummary.minLenInSuit(s);
        if (!meLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        OptionalInt partnerLenLonger = getPlayer(position.getOpposite()).infSummary.minLenInSuit(s);
        if (!partnerLenLonger.isPresent()) {
            return OptionalInt.empty();
        }
        int meLen = meLenLonger.getAsInt();
        int partnerLen = partnerLenLonger.getAsInt();
        return OptionalInt.of(meLen + partnerLen);
    }
    
    public boolean bidSuit(Position position, int s) {
        OptionalInt bidSuits = getPlayer(position).infSummary.getBidSuits();
        if (bidSuits.isEmpty()) {
            return false;
        }
        if ((bidSuits.getAsInt() & (1 << s)) == 0) {
            return false;
        }
        return true;
    }
}
