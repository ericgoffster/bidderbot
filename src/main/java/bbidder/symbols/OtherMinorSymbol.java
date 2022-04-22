package bbidder.symbols;

import static bbidder.Constants.MINORS;

import java.util.Comparator;
import java.util.Map;

import bbidder.Constants;
import bbidder.Symbol;

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
    public Map<String, Integer> unevaluate(int strain) {
        return Map.of("om", strain);
    }

    @Override
    public short getSuitClass(Map<String, Integer> suits) {
        if (suits.containsKey("m") || suits.containsKey("om")) {
            return (short) (1 << evaluate(suits));
        }
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
    
    @Override
    public Comparator<Integer> direction() {
        return Integer::compare;
    }    

    @Override
    public boolean levelAllowed(int level) {
        return true;
    }
}
