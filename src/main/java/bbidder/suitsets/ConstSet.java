package bbidder.suitsets;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetContext;
import bbidder.SuitTable;

public final class ConstSet implements SuitSet {
    private final String ssuits;
    private final short suits;

    public ConstSet(String ssuits, short suits) {
        this.ssuits = ssuits;
        this.suits = suits;
    }

    @Override
    public short evaluate(Players players) {
        return suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssuits, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstSet other = (ConstSet) obj;
        return Objects.equals(ssuits, other.ssuits) && suits == other.suits;
    }

    @Override
    public String toString() {
        return ssuits;
    }

    @Override
    public Stream<SuitSetContext> resolveSymbols(SuitTable suitTable) {
        return Stream.of(new SuitSetContext(this, suitTable));
    }
}