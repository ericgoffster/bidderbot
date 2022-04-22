package bbidder;

import static bbidder.Constants.MINORS;

import java.util.Map;

public class MinorSymbol implements Symbol {
    public MinorSymbol() {
        super();
    }
    
    @Override
    public String toString() {
        return "m";
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
        if (suits.containsKey("om")) {
            return otherMinor(suits.get("om"));
        }
        return suits.get("m");
    }
    
    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public void unevaluate(Map<String, Integer> suits, int strain) {
        suits.put("m", strain);        
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
