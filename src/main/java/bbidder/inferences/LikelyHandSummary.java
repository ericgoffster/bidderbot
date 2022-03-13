package bbidder.inferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bbidder.Constants;

public class LikelyHandSummary {
    public final LikelySuitSummary suitSummaries[];

    public LikelyHandSummary(LikelySuitSummary[] suitSummaries) {
        super();
        this.suitSummaries = suitSummaries;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            if (!suitSummaries[i].isEmpty()) {
                list.add(Constants.STR_ALL_SUITS.charAt(i) + ":" + suitSummaries[i]);
            }
        }
        return String.join(",", list);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(suitSummaries);
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
        return Arrays.equals(suitSummaries, other.suitSummaries);
    }
    
}