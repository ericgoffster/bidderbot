package bbidder.inferences;

import java.util.List;
import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.ListUtil;
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
    public final Symbol suit;
    public final SuitSet among;

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
    public List<InferenceContext> resolveSymbols(SymbolTable symbols) {
        return ListUtil.flatMap(suit.resolveSymbols(symbols), e1 -> ListUtil.map(among.resolveSymbols(e1.symbols),
                e2 -> new InferenceContext(new LongestOrEqual(e1.getSymbol(), e2.suitSet), e2.symbols)));
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
