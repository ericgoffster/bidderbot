package bbidder.inferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import bbidder.Constants;

public class LikelyHandSummary {
    public int minTotalPoints;
    public int minHcp;
    public final LikelySuitSummary suitSummaries[];

    public LikelyHandSummary(int minTotalPoints, int minHcp, LikelySuitSummary[] suitSummaries) {
        super();
        this.minTotalPoints = minTotalPoints;
        this.minHcp = minHcp;
        this.suitSummaries = suitSummaries;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        list.add("tpts="+minTotalPoints);
        list.add("hcp="+minHcp);
        for(int i = 0; i < 5; i++) {
            list.add(Constants.STR_ALL_SUITS.charAt(i) + ":" + suitSummaries[i]);
        }
        return String.join(",", list);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(suitSummaries);
        result = prime * result + Objects.hash(minHcp, minTotalPoints);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LikelyHandSummary other = (LikelyHandSummary) obj;
        return minHcp == other.minHcp && minTotalPoints == other.minTotalPoints && Arrays.equals(suitSummaries, other.suitSummaries);
    }
}