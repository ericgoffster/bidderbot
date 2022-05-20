package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class NotInference extends Inference {
    private final Inference i1;

    private NotInference(Inference i1) {
        this.i1 = i1;
    }

    @Override
    public IBoundInference bind(Players players) {
        return i1.bind(players).negate();
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return i1.resolveSuits(suitTable).map(e1 -> new NotInference(e1.getInference()).new Context(e1.suitTable));
    }

    public static Inference create(Inference i1) {
        return new NotInference(i1);
    }

    @Override
    public String toString() {
        return "~" + i1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NotInference other = (NotInference) obj;
        return Objects.equals(i1, other.i1);
    }
}
