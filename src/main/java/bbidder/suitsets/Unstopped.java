package bbidder.suitsets;

import java.util.Optional;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.SuitTable;

public final class Unstopped implements SuitSet {
    public Unstopped() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        Optional<Short> partnerSuits = players.partner.infSummary.getBidSuits();
        Optional<Short> meSuits = players.me.infSummary.getBidSuits();
        if (!partnerSuits.isPresent() || !meSuits.isPresent()) {
            return 0;
        }
        short allBid = (short) (partnerSuits.get() | meSuits.get());
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
    public Stream<SuitSetContext> resolveSuits(SuitTable suitTable) {
        return Stream.of(new SuitSetContext(this, suitTable));
    }

    @Override
    public String toString() {
        return "unstopped";
    }
}