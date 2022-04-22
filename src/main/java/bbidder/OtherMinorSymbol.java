package bbidder;

import static bbidder.Constants.MINORS;

import java.util.Map;

public class OtherMinorSymbol implements Symbol {
    public OtherMinorSymbol() {
        super();
    }
    
    @Override
    public String toString() {
        return "om";
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
        if (suits.containsKey("m")) {
            return otherMinor(suits.get("m"));
        }
        return suits.get("om");
    }
    
    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public void unevaluate(Map<String, Integer> suits, int strain) {
        suits.put("om", strain);        
    }
    
    @Override
    public short getSuitClass() {
        return MINORS;
    }

    private static Integer otherMinor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.CLUB:
            return Constants.DIAMOND;
        case Constants.DIAMOND:
            return Constants.CLUB;
        default:
            throw new IllegalArgumentException("invalid minor");
        }
    }
}
