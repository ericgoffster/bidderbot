package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Players;
import bbidder.SymbolTable;
import bbidder.inferences.bound.AndBoundInf;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public class AndInference implements Inference {
    public final Inference i1;
    public final Inference i2;

    private AndInference(Inference i1, Inference i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    @Override
    public IBoundInference bind(Players players) {
        return AndBoundInf.create(i1.bind(players), i2.bind(players));
    }

    @Override
    public List<InferenceContext> resolveSymbols(SymbolTable suits) {
        List<InferenceContext> res = new ArrayList<>();
        for (InferenceContext bc : i1.resolveSymbols(suits)) {
            for (InferenceContext bc2 : i2.resolveSymbols(bc.suits)) {
                res.add(new InferenceContext(new AndInference(bc.inference, bc2.inference), bc2.suits));
            }
        }
        return res;
    }

    public static Inference create(Inference i1, Inference i2) {
        if (i1.equals(TrueInference.T)) {
            return i2;
        }
        if (i2.equals(TrueInference.T)) {
            return i1;
        }
        return new AndInference(i1, i2);
    }

    public static Inference create(List<Inference> l) {
        if (l.isEmpty()) {
            return TrueInference.T;
        }
        if (l.size() == 1) {
            return l.get(0);
        }
        return create(l.get(0), create(l.subList(1, l.size())));
    }

    @Override
    public String toString() {
        return i1 + "," + i2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i1, i2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AndInference other = (AndInference) obj;
        return Objects.equals(i1, other.i1) && Objects.equals(i2, other.i2);
    }
}
