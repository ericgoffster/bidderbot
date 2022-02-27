package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HandGenerator {
    public static HandList generateHands(IBoundInference inf, int numDesired) {
        Random r = new Random();
        int[] cards = new int[52];
        for(int i = 0; i < 52; i++) {
            cards[i] = i;
        }
        List<Hand> result = new ArrayList<>();
        while(result.size() < numDesired) {
            short[] suits = new short[4];
            for(int i = 0; i < 13; i++) {
                int p = r.nextInt(52 - i) + i;
                int t = cards[p];
                cards[p] = cards[i];
                cards[i] = t;
                int suit = t % 4;
                int rank = t / 4;
                suits[suit] |= (1 << rank);
            }
            Hand h = new Hand(suits);
            if (inf.matches(h)) {
                result.add(h);
            }
        }
        return new HandList(result);
    }
}
