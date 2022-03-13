package bbidder;

public class Players {
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
    
}
