package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
import bbidder.ShapeSet;

public class HcpBoundInf implements IBoundInference {
    final Range r;
    
    @Override
    public ShapeSet getShapes() {
        return ShapeSet.ALL;
    }
    
    @Override
    public ShapeSet getNotShapes() {
        return ShapeSet.ALL;
    }
    
    public HcpBoundInf(Range r) {
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        return r.contains(hand.numHCP());
    }

    @Override
    public String toString() {
        return r + " hcp";
    }
}