package bbidder;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;

public interface Symbol {
    public Integer evaluate(Map<String, Integer> suits);

    public Map<String, Integer> unevaluate(int strain);

    public short getSuitClass(Map<String, Integer> suits);

    public int getResolved();
    
    public Comparator<Integer> direction();
    
    public Predicate<Integer> levelTest();
}
