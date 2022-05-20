package bbidder;

import java.util.function.BiPredicate;

import bbidder.utils.MyStream;

/**
 * Represents a generality.
 * A generality evaluates an auction and state, returning
 * true if it matches.
 * 
 * @author goffster
 *
 */
public abstract class Generality implements BiPredicate<Players, Auction> {
    public abstract MyStream<Context> resolveSuits(SuitTable suitTable);

    public final class Context {
        public final SuitTable suitTable;

        public Context(SuitTable suitTable) {
            super();
            this.suitTable = suitTable;
        }

        public Generality getGenerality() {
            return Generality.this;
        }
    }
}
