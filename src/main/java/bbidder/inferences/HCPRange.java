package bbidder.inferences;

import java.util.List;
import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Players;
import bbidder.Range;
import bbidder.SymbolTable;
import bbidder.inferences.bound.HcpBoundInf;

/**
 * Represents the inference for a high card point range.
 * 
 * @author goffster
 *
 */
public final class HCPRange implements Inference {
    public final Range rng;

    public HCPRange(Integer min, Integer max) {
        super();
        this.rng = Range.between(min, max, 40);
    }

    public HCPRange(Range r) {
        super();
        this.rng = r;
    }

    @Override
    public IBoundInference bind(Players players) {
        return HcpBoundInf.create(rng);
    }

    @Override
    public List<InferenceContext> resolveSymbols(SymbolTable suits) {
        return List.of(new InferenceContext(this, suits));
    }

    public static Inference valueOf(String str) {
        RangeOf rng = RangeOf.valueOf(str);
        if (rng == null) {
            return null;
        }
        if (rng.of.equalsIgnoreCase("hcp")) {
            return new HCPRange(Range.between(rng.min, rng.max, 40));
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return rng + " hcp";
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HCPRange other = (HCPRange) obj;
        return Objects.equals(rng, other.rng);
    }
}
