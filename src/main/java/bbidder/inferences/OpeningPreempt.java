package bbidder.inferences;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.HcpBoundInf;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.SplitUtil;

/**
 * Represents the inference of a premptive hand of varying levels.
 * 
 * @author goffster
 *
 */
public final class OpeningPreempt extends Inference {
    private final Symbol symbol;
    private final int level;

    public OpeningPreempt(Symbol suit, int level) {
        super();
        this.symbol = suit;
        this.level = level;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = symbol.getResolved();
        return AndBoundInf.create(HcpBoundInf.create(Range.between(5, 10, 40)),
                ShapeBoundInf.create(ShapeSet.create(shape -> isPremptive(strain, level, shape))));
    }

    @Override
    public Stream<Context> resolveSymbols(SymbolTable symbols) {
        return symbol.resolveSymbols(symbols).map(e -> new OpeningPreempt(e.getSymbol(), level).new Context(e.symbols));
    }

    public static OpeningPreempt valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 3);
        if (parts.length != 3) {
            return null;
        }
        if (!parts[0].equalsIgnoreCase("opening_preempt")) {
            return null;
        }
        Symbol sym = SymbolParser.parseSymbol(parts[2]);
        if (sym == null) {
            return null;
        }
        return new OpeningPreempt(sym, Integer.parseInt(parts[1]));
    }

    private static boolean isPremptive(int suit, int level, Shape hand) {
        int len = hand.numInSuit(suit);
        switch (level) {
        case 2:
            return len == 6;
        case 3:
            return len == 7;
        case 4:
            return len == 8;
        case 5:
            return len > 8;
        default:
            throw new IllegalStateException("Invalid level");
        }
    }

    @Override
    public String toString() {
        return "opening_preempt " + level + " " + symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OpeningPreempt other = (OpeningPreempt) obj;
        return level == other.level && Objects.equals(symbol, other.symbol);
    }
}
