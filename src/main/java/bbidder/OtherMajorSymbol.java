package bbidder;

import static bbidder.Constants.MAJORS;

import java.util.Map;

public class OtherMajorSymbol implements Symbol {
    public OtherMajorSymbol() {
        super();
    }
    
    @Override
    public String toString() {
        return "OM";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
    
    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        if (suits.containsKey("M")) {
            return otherMajor(suits.get("M"));
        }
        return suits.get("OM");
    }
    
    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public void unevaluate(Map<String, Integer> suits, int strain) {
        suits.put("OM", strain);        
    }
    
    @Override
    public short getSuitClass() {
        return MAJORS;
    }

    private static Integer otherMajor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.HEART:
            return Constants.SPADE;
        case Constants.SPADE:
            return Constants.HEART;
        default:
            throw new IllegalArgumentException("invalid major");
        }
    }
}
