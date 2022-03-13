package bbidder.inferences;

import java.util.Objects;

public class LikelySuitSummary {
    public final int minTotalPoints;
    public final int minLength;

    public LikelySuitSummary(int minTotalPoints, int minLength) {
        super();
        this.minTotalPoints = minTotalPoints;
        this.minLength = minLength;
    }

    @Override
    public String toString() {
        return "minTotalPoints=" + minTotalPoints + ", minLength=" + minLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minLength, minTotalPoints);
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
        return minLength == other.minLength && minTotalPoints == other.minTotalPoints;
    }
}