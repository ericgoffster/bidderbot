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
    public abstract List<Context> resolveSymbols(SymbolTable symbols);
    
    public final class Context {
        public final SymbolTable symbols;

        public Context(SymbolTable symbols) {
            super();
            this.symbols = symbols;
        }

        public Generality getGenerality() {
            return Generality.this;
        }
    }
}
