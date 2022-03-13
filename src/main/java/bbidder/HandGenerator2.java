package bbidder;

import java.util.Map;
import java.util.Random;

/**
 * Generates a set of hands consistent with the inference.
 * 
 * @author goffster
 *
 */
public class HandGenerator2 {
    public static void generateHands(Random r, Map<Shape, Integer> num, int numDesired) {
        int[] cards = new int[52];
        for (int i = 0; i < 52; i++) {
            cards[i] = i;
        }
        while (numDesired > 0) {
            Hand h = new Hand();
            for (int i = 0; i < 13; i++) {
                int p = r.nextInt(52 - i) + i;
                int t = cards[p];
                cards[p] = cards[i];
                cards[i] = t;
                h = h.withCardAdded(t % 4, t / 4);
            }
            Shape s = h.getShape();
            Integer i = num.get(s);
            if (i == null) {
                i = 0;
            }
            num.put(s, i + 1);
            numDesired--;
        }
    }
}
