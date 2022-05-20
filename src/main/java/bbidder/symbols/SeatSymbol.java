package bbidder.symbols;

import java.util.Objects;

import bbidder.Bid;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class SeatSymbol extends Symbol {
    public static final String NAME = "seat";
    private final Symbol symbol;
    private final short seats;

    public SeatSymbol(Symbol symbol, short seats) {
        super();
        this.symbol = symbol;
        this.seats = seats;
    }

    @Override
    public boolean downTheLine() {
        return symbol.downTheLine();
    }

    @Override
    public String toString() {
        return symbol + ":" + NAME + seats;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seats, symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SeatSymbol other = (SeatSymbol) obj;
        return seats == other.seats && Objects.equals(symbol, other.symbol);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new SeatSymbol(e.getSymbol(), seats).new Context(e.suitTable));
    }

    @Override
    public int getResolvedStrain() {
        return symbol.getResolvedStrain();
    }

    @Override
    public boolean compatibleWith(Bid bid) {
        return symbol.compatibleWith(bid);
    }

    @Override
    public short getSeats() {
        return seats;
    }

    @Override
    public boolean isNonConvential() {
        return false;
    }
}
