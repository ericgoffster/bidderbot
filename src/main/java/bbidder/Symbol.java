package bbidder;

import java.util.List;
import java.util.Map;

public interface Symbol {
    public Integer evaluate(Map<String, Integer> suits);

    public Map<String, Integer> unevaluate(int strain);

    public List<Symbol> boundSymbols(Map<String, Integer> suits);

    public int getResolved();
    
    public boolean compatibleWith(Bid bid);
}
