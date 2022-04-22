package bbidder;

import static bbidder.Constants.ALL_SUITS;
import static bbidder.Constants.MAJORS;
import static bbidder.Constants.MINORS;

import java.util.Map;
import java.util.Objects;

public class VarSymbol implements Symbol {
    public final String v;
    
    public VarSymbol(String v) {
        super();
        this.v = v;
    }
    
    @Override
    public String toString() {
        return v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VarSymbol other = (VarSymbol) obj;
        return Objects.equals(v, other.v);
    }
    
    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        if (v.equals("M") && suits.containsKey("OM")) {
            return otherMajor(suits.get("OM"));
        }
        if (v.equals("m") && suits.containsKey("om")) {
            return otherMinor(suits.get("om"));
        }
        if (v.equals("OM") && suits.containsKey("M")) {
            return otherMajor(suits.get("M"));
        }
        if (v.equals("om") && suits.containsKey("m")) {
            return otherMinor(suits.get("m"));
        }
        return suits.get(v);
    }
    
    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public void unevaluate(Map<String, Integer> suits, int strain) {
        suits.put(v, strain);        
    }
    
    @Override
    public short getSuitClass() {
        switch (v) {
        case "M":
            return MAJORS;
        case "OM":
            return MAJORS;
        case "m":
            return MINORS;
        case "om":
            return MINORS;
        default:
            return ALL_SUITS;
        }
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
