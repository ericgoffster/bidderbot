package bbidder.suitsets;

import java.util.Optional;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.SuitTable;

public final class Unbid implements SuitSet {
    public Unbid() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        Optional<Short> lhoSuits = players.lho.infSummary.getBidSuits();
        Optional<Short> rhoSuits = players.rho.infSummary.getBidSuits();
        Optional<Short> partnerSuits = players.partner.infSummary.getBidSuits();
        Optional<Short> meSuits = players.me.infSummary.getBidSuits();
        if (!lhoSuits.isPresent() || !rhoSuits.isPresent() || !partnerSuits.isPresent() || !meSuits.isPresent()) {
            return 0;
        }
        short allBid = (short) (lhoSuits.get() | rhoSuits.get() | partnerSuits.get() | meSuits.get());
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
        return "unbid";
    }
}