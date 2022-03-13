package bbidder.inferences.bound;

import java.util.ArrayList;
import java.util.List;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.ShapeSet;

/**
 * Represents the "or" of 2 or more bound inferences.
 * 
 * @author goffster
 *
 */
public class NorBoundInf implements IBoundInference {
    public final List<IBoundInference> inferences;

    private NorBoundInf(List<IBoundInference> inf) {
        super();
        this.inferences = inf;
    }
    
    @Override
    public boolean matches(Hand hand) {
        for (IBoundInference i : inferences) {
            if (i.matches(hand)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ShapeSet getShapes() {
        ShapeSet s = ShapeSet.ALL;
        for(IBoundInference i: inferences) {
            s = s.and(i.getNotShapes());
        }
        return s;
    }
    
    @Override
    public ShapeSet getNotShapes() {
        ShapeSet s = ShapeSet.NONE;
        for(IBoundInference i: inferences) {
            s = s.or(i.getShapes());
        }
        return s;
    }

    public static IBoundInference create(IBoundInference i1, IBoundInference i2) {
        return create(List.of(i1, i2));
    }
    
    public static IBoundInference create(List<IBoundInference> inferences) {
        List<IBoundInference> orList = new ArrayList<>();
        for (IBoundInference i : inferences) {
            orList.add(i);
        }
        if (orList.size() == 0) {
            return ConstBoundInference.T;
        }
        return new NorBoundInf(orList);
    }

    @Override
    public String toString() {
        List<String> l = new ArrayList<>();
        for (IBoundInference i : inferences) {
            l.add(i.toString());
        }
        return "~(" + String.join(" | ", l) + ")";
    }
}
