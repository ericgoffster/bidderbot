package bbidder.suitsets;

import java.util.Map;

import bbidder.Players;
import bbidder.SuitSet;

public class Unbid implements SuitSet {
    public Unbid() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        short allBid = (short)(players.lho.infSummary.getBidSuits() | players.rho.infSummary.getBidSuits()
                | players.partner.infSummary.getBidSuits() | players.me.infSummary.getBidSuits());
        return (short)(0xf ^ allBid);
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
    public SuitSet replaceVars(Map<String, Integer> bc) {
        return this;
    }
    
    @Override
    public String toString() {
        return "unbid";
    }
}