package bbidder;

import java.util.Map;

public interface Symbol {
    public Integer evaluate(Map<String, Integer> suits);
    public void unevaluate(Map<String, Integer> suits, int strain);
    public short getSuitClass();
    public int getResolved();
}
