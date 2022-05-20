package bbidder.generalities;

import java.util.Objects;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class IsTwoSuitedGenerality extends Generality {
    public static final String NAME = "two_suited";
    private final Symbol symbol1;
    private final Symbol symbol2;
    private final int position;

    public IsTwoSuitedGenerality(Symbol symbol1, Symbol symbol2, int pos) {
        super();
        this.symbol1 = symbol1;
        this.symbol2 = symbol2;
        this.position = pos;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol1.resolveSuits(suitTable)
                .flatMap(e1 -> symbol2.resolveSuits(e1.suitTable)
                        .map(e2 -> new IsTwoSuitedGenerality(e1.getSymbol(), e2.getSymbol(), position).new Context(e2.suitTable)));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int s1 = symbol1.getResolvedStrain();
        int s2 = symbol2.getResolvedStrain();
        Players pl = players.rotate(position);
        return pl.iBidSuit(s1) && pl.iBidSuit(s2);
    }
    
    public String getPosName() {
        switch(position) {
        case 0: return "i_am";
        case 1: return "rho_is";
        case 2: return "partner_is";
        case 3: return "lho_is";
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(position, symbol1, symbol2);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        IsTwoSuitedGenerality other = (IsTwoSuitedGenerality) obj;
        return position == other.position && Objects.equals(symbol1, other.symbol1) && Objects.equals(symbol2, other.symbol2);
    }

    @Override
    public String toString() {
        return getPosName() + "_" + NAME + " " + symbol1 + " " + symbol2;
    }

}
