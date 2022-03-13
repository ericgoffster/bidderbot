package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates a set of hands consistent with the inference.
 * 
 * @author goffster
 *
 */
public class HandGenerator {
    public static HandList generateHands(Random r, IBoundInference inf, int numDesired, InfSummary partner) {
        int[] cards = new int[52];
        for (int i = 0; i < 52; i++) {
            cards[i] = i;
        }
        InfSummary summary = inf.getSummary();
        List<Hand> result = new ArrayList<>();
        int num = 0;
        while (result.size() < numDesired) {
            Hand h = new Hand();
            for (int i = 0; i < 13; i++) {
                int p = r.nextInt(52 - i) + i;
                int t = cards[p];
                cards[p] = cards[i];
                cards[i] = t;
                h = h.withCardAdded(t % 4, t / 4);
            }
            if (summary.shape.contains(h.getShape())
                    && summary.tpts.contains(h.getTotalPoints(partner))
                    && inf.matches(h)) {
                result.add(h);
            }
            num++;
            if (num > 1000000 && result.size() == 0) {
                throw new RuntimeException("no hand found for " + inf);
            }
        }
        return new HandList(result);
    }
}
