package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.SplitUtil;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.HcpBoundInf;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.symbols.ConstSymbol;

/**
 * Represents the inference of a premptive hand of varying levels.
 * 
 * @author goffster
 *
 */
public class OpeningPreempt implements Inference {
    private final Symbol suit;
    private final int level;

    public OpeningPreempt(Symbol suit, int level) {
        super();
        this.suit = suit;
        this.level = level;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = suit.getResolved();
        return AndBoundInf.create(HcpBoundInf.create(Range.between(5, 10, 40)),
                ShapeBoundInf.create(ShapeSet.create(shape -> isPremptive(strain, level, shape))));
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext context) {
        List<BiddingContext> l = new ArrayList<>();
        for (var e : context.resolveSymbols(suit).entrySet()) {
            l.add(e.getValue().withInferenceAdded(new OpeningPreempt(new ConstSymbol(e.getKey()), level)));
        }
        return l;
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
        return "opening_preempt " + level + " " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, suit);
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
        return level == other.level && Objects.equals(suit, other.suit);
    }
}
