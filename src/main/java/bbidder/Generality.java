package bbidder;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * Represents a generality.
 * A generality evaluates an auction and state, returning
 * true if it matches.
 * @author goffster
 *
 */
public interface Generality extends BiPredicate<Players, Auction> {
    public List<GeneralityContext> resolveSymbols(SymbolTable symbols);
}
