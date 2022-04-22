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
public abstract class Generality implements BiPredicate<Players, Auction> {
    public abstract List<GeneralityContext> resolveSymbols(SymbolTable symbols);
    
    public static final class GeneralityContext {
        public final Generality generality;
        public final SymbolTable symbols;

        public GeneralityContext(Generality generality, SymbolTable symbols) {
            super();
            this.generality = generality;
            this.symbols = symbols;
        }
    }
}
