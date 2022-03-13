package bbidder.inferences;

import java.util.Objects;

public class LikelySuitSummary {
    public final int minLength;

    public LikelySuitSummary(int minLength) {
        super();
        this.minLength = minLength;
    }

    @Override
    public String toString() {
        return "" + minLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minLength);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LikelySuitSummary other = (LikelySuitSummary) obj;
        return minLength == other.minLength;
    }
}