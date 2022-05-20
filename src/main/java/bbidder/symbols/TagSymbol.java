package bbidder.symbols;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import bbidder.Bid;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class TagSymbol extends Symbol {
    private final Symbol symbol;
    private final String tag;

    public TagSymbol(String tag, Symbol symbol) {
        super();
        this.tag = tag;
        this.symbol = symbol;
    }

    @Override
    public Set<String> getTags() {
        Set<String> s = new HashSet<>(symbol.getTags());
        s.add(tag);
        return s;
    }
    
    @Override
    public boolean downTheLine() {
        return symbol.downTheLine();
    }

    @Override
    public String toString() {
        return symbol + ":\"" + tag + "\"";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(symbol, tag);
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
        TagSymbol other = (TagSymbol) obj;
        return Objects.equals(symbol, other.symbol) && Objects.equals(tag, other.tag);
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
    public boolean isNonConvential() {
        return symbol.isNonConvential();
    }

    @Override
    public short getSeats() {
        return symbol.getSeats();
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new TagSymbol(tag, e.getSymbol()).new Context(e.suitTable));
    }
}
