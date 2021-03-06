package bbidder;

import bbidder.symbols.ConstSymbol;
import bbidder.utils.MyStream;

/**
 * Represents a symbol that resolves to a strain.
 * 
 * @author goffster
 *
 */
public abstract class Symbol {
    /**
     * @return The resolved strain
     */
    public abstract int getResolvedStrain();

    /**
     * Resolves the symbol into a stream of resolved symbols in the context of a suit table.
     * For example:
     * If my symbol was "M", and the suitTable already had M=SPADE,
     * then this would return a stream of a single item, with a resolved symbol of SPADE.
     * If the suitTable was empty,
     * then this would return a stream of two items,
     * one with a resolved symbol of HEART (with a symbol table of M=HEART)
     * the other with a resolved symbol of SPADE (with a symbol table of M=SPADE)
     * 
     * @param suitTable
     *            The suit table to lookup
     * @return Stream of context's
     */
    public abstract MyStream<ConstSymbol.Context> resolveSuits(SuitTable suitTable);

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
