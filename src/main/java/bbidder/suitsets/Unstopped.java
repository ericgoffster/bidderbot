package bbidder.suitsets;

import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.SymbolTable;

public final class Unstopped implements SuitSet {
    public Unstopped() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        short allBid = (short) (players.me.infSummary.getBidSuits() | players.partner.infSummary.getBidSuits());
        for (int i = 0; i < 4; i++) {
            if (players.me.infSummary.stoppers.stopperIn(i) || players.partner.infSummary.stoppers.stopperIn(i)
                    || players.me.infSummary.partialStoppers.stopperIn(i) || players.partner.infSummary.partialStoppers.stopperIn(i)) {
                allBid |= (short) (1 << i);
            }
        }
        return (short) (0xf ^ allBid);
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
    public Stream<SuitSetContext> resolveSymbols(SymbolTable symbols) {
        return Stream.of(new SuitSetContext(this, symbols));
    }

    @Override
    public String toString() {
        return "unstopped";
    }
}