package bbidder;

import java.util.Map;
import java.util.Objects;

public class ConstSymbol implements Symbol {
    public final int strain;

    public ConstSymbol(int strain) {
        super();
        this.strain = strain;
    }
    
    @Override
    public String toString() {
        return Strain.getName(strain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strain);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstSymbol other = (ConstSymbol) obj;
        return strain == other.strain;
    }

    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        return strain;
    }
    
    @Override
    public void unevaluate(Map<String, Integer> suits, int strain) {
        if (strain != this.strain) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public short getSuitClass() {
        return (short) (1 << strain);
    }
    
    @Override
    public int getResolved() {
        return strain;
    }
}
