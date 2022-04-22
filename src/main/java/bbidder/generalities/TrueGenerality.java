package bbidder.generalities;

import java.util.List;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.GeneralityContext;
import bbidder.Players;
import bbidder.SymbolTable;

public final class TrueGenerality implements Generality {
    public static TrueGenerality T = new TrueGenerality();

    private TrueGenerality() {
    }

    @Override
    public List<GeneralityContext> resolveSymbols(SymbolTable bc) {
        return List.of(new GeneralityContext(this, bc));
    }

    @Override
    public boolean matches(Players players, Auction bidList) {
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == T;
    }
}
