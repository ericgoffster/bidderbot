package bbidder.inferences;

import bbidder.BitUtil;
import bbidder.Context;
import bbidder.Hand;
import bbidder.Inference;

public class Balanced implements Inference {
    public static Balanced valueOf(String str) {
        str = str.trim();
        if (str.toLowerCase().equals("balanced")) {
            return new Balanced();
        }
        return null;
    }

    public Balanced() {
        super();
    }

    @Override
    public boolean matches(Context context, Hand hand) {
        int ndoub = 0;
        for (int s = 0; s < 4; s++) {
            int len = BitUtil.size(hand.suits[s]);
            if (len < 2) {
                return false;
            }
            if (len == 2) {
                ndoub++;
            }
        }
        return ndoub <= 1;
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
    public String toString() {
        return "balanced";
    }
}
