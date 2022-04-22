package bbidder;

import java.util.Map;

public interface SuitSet {
    public short evaluate(Players players);

    public SuitSet replaceVars(Map<String, Integer> bc);
}