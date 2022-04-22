package bbidder;

import java.util.List;

public interface SuitSet {
    public short evaluate(Players players);

    public List<SuitSetContext> resolveSymbols(SymbolTable symbols);
}