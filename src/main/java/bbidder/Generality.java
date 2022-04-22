package bbidder;

import java.util.List;

public interface Generality {
    public List<GeneralityContext> resolveSymbols(SymbolTable context);

    public boolean matches(Players players, Auction bidList);
}
