package bbidder.inferences;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitSet;
import bbidder.SuitSets;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a suit than is longest or equal among other suits.
 * 
 * @author goffster
 *
 */
public final class LongestOrEqual extends Inference {
    private final Symbol suit;
    private final SuitSet among;

    public LongestOrEqual(Symbol suit, SuitSet among) {
        this.suit = suit;
        this.among = among;
    }

    @Override
    public IBoundInference bind(Players players) {
        int iamong = among == null ? 0xf : among.evaluate(players);
        int strain = suit.getResolved();
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isLongerOrEqual(strain, iamong)));
    }

    @Override
    public Stream<Context> resolveSymbols(SymbolTable symbols) {
        return suit.resolveSymbols(symbols)
                .flatMap(e1 -> among.resolveSymbols(e1.symbols).map(e2 -> new LongestOrEqual(e1.getSymbol(), e2.suitSet).new Context(e2.symbols)));
    }

    public static LongestOrEqual valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (!str.toLowerCase().startsWith("longest_or_equal")) {
            return null;
        }
        str = str.substring(16).trim();
        int pos = str.indexOf("among");
        if (pos >= 0) {
            Symbol sym = SymbolParser.parseSymbol(str.substring(0, pos).trim());
            if (sym == null) {
                return null;
            }
            return new LongestOrEqual(sym, SuitSets.lookupSuitSet(str.substring(pos + 5).trim()));
        }
        Symbol sym = SymbolParser.parseSymbol(str);
        if (sym == null) {
            return null;
        }
        return new LongestOrEqual(sym, null);
    }

    @Override
    public String toString() {
        return "longest_or_equal " + suit + (among == null ? "" : " among " + among);
    }

    @Override
    public int hashCode() {
        return Objects.hash(among, suit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LongestOrEqual other = (LongestOrEqual) obj;
        return Objects.equals(among, other.among) && Objects.equals(suit, other.suit);
    }
}
