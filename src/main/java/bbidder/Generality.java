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
public abstract class Generality implements BiPredicate<Players, TaggedAuction> {
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

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
